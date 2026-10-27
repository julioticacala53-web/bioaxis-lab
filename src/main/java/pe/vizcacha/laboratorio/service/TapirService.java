package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.domain.Tapir;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.domain.enums.EquipmentStatus;
import pe.vizcacha.laboratorio.domain.enums.TapirState;
import pe.vizcacha.laboratorio.repo.TapirRepository;
import pe.vizcacha.laboratorio.repo.AlpacaRepository;
import pe.vizcacha.laboratorio.dto.TapirForm;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TapirService {

    private final TapirRepository maintenance;
    private final AlpacaRepository resources;
    private final BuhoService audit;
    private final SessionService session;
    private final IdSequence ids;

    public TapirService(TapirRepository maintenance, AlpacaRepository resources,
                              BuhoService audit, SessionService session, IdSequence ids) {
        this.maintenance = maintenance;
        this.resources = resources;
        this.audit = audit;
        this.session = session;
        this.ids = ids;
    }

    @Transactional
    public Tapir save(TapirForm form) {
        boolean nuevo = isBlank(form.id());
        String id = nuevo ? ids.next("MAN-", maintenance.findAllIds()) : form.id();

        LocalDate inicio = requiredDate(form.start(), "inicio");
        LocalDate fin = requiredDate(form.end(), "finalización estimada");
        LocalDate real = optionalDate(form.actual());
        TapirState estado = TapirState.fromLabel(form.state());

        if (fin.isBefore(inicio) || (real != null && real.isBefore(inicio))) {
            throw new BusinessException("La finalización no puede ser anterior al inicio.");
        }
        if (estado == TapirState.FINALIZADO && real == null) {
            throw new BusinessException("Registra la fecha real de finalización.");
        }
        if (estado == TapirState.EN_PROCESO
                && maintenance.findFirstByEquipmentIdAndState(
                        form.equipment(), TapirState.EN_PROCESO)
                .filter(otro -> !otro.getId().equals(id))
                .isPresent()) {
            throw new BusinessException("Este equipo ya tiene un mantenimiento en proceso.");
        }

        Alpaca equipoNuevo = resources.findById(form.equipment())
                .orElseThrow(() -> new BusinessException("El equipo indicado no existe."));

        Tapir m;
        Alpaca equipoAnterior = null;
        if (nuevo) {
            m = new Tapir();
            m.setId(id);
        } else {
            m = maintenance.findById(id).orElseThrow(() ->
                    new BusinessException("El mantenimiento indicado no existe."));

            equipoAnterior = m.getEquipment();
        }

        m.setEquipment(equipoNuevo);
        m.setType(trim(form.type()));
        m.setStartDate(inicio);
        m.setEndDate(fin);
        m.setActualDate(real);
        m.setResponsible(trim(form.responsible()));
        m.setProvider(trim(form.provider()));
        m.setDescription(trim(form.description()));
        m.setState(estado);

        Tapir guardado = maintenance.save(m);

        applyCascade(equipoNuevo, equipoAnterior, estado, real, inicio);

        audit.record(session.userName(), session.role(),
                "Mantenimiento actualizado", "Mantenimiento", guardado.getId());

        return guardado;
    }

    private void applyCascade(Alpaca equipoNuevo, Alpaca equipoAnterior,
                              TapirState estadoGuardado, LocalDate fechaReal,
                              LocalDate inicio) {
        Set<Alpaca> afectados = new LinkedHashSet<>();
        afectados.add(equipoNuevo);
        if (equipoAnterior != null) {
            afectados.add(equipoAnterior);
        }

        for (Alpaca eq : afectados) {

            Optional<Tapir> activo = maintenance.findFirstByEquipmentIdAndState(
                    eq.getId(), TapirState.EN_PROCESO);

            if (activo.isPresent()) {
                eq.setStatus(EquipmentStatus.EN_MANTENIMIENTO);
                eq.setNextMaint(activo.get().getEndDate());
            } else if (eq.getStatus() == EquipmentStatus.EN_MANTENIMIENTO) {
                eq.setStatus(EquipmentStatus.OPERATIVO);
                if (estadoGuardado == TapirState.FINALIZADO) {
                    eq.setLastMaint(fechaReal);
                }
            }

            if (eq.getId().equals(equipoNuevo.getId())
                    && estadoGuardado == TapirState.PROGRAMADO) {
                eq.setNextMaint(inicio);
            }
        }
    }

    private LocalDate requiredDate(String raw, String campo) {
        if (isBlank(raw)) {
            throw new BusinessException("Indica la fecha de " + campo + ".");
        }
        return LocalDate.parse(raw.trim());
    }

    private LocalDate optionalDate(String raw) {
        return isBlank(raw) ? null : LocalDate.parse(raw.trim());
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
