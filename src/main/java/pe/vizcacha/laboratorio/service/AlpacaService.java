package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.domain.enums.EquipmentStatus;
import pe.vizcacha.laboratorio.domain.enums.TapirState;
import pe.vizcacha.laboratorio.domain.enums.AlpacaKind;
import pe.vizcacha.laboratorio.dto.MovementForm;
import pe.vizcacha.laboratorio.dto.AlpacaForm;
import pe.vizcacha.laboratorio.repo.TapirRepository;
import pe.vizcacha.laboratorio.repo.AlpacaRepository;

import java.time.LocalDate;

@Service
public class AlpacaService {

    private static final String ENTRADA = "IN";

    private final AlpacaRepository resources;
    private final TapirRepository maintenance;
    private final BuhoService audit;
    private final SessionService session;
    private final IdSequence ids;

    public AlpacaService(AlpacaRepository resources, TapirRepository maintenance,
                           BuhoService audit, SessionService session, IdSequence ids) {
        this.resources = resources;
        this.maintenance = maintenance;
        this.audit = audit;
        this.session = session;
        this.ids = ids;
    }

    @Transactional
    public Alpaca save(AlpacaForm form) {
        AlpacaKind kind = AlpacaKind.fromLabel(form.kind());
        boolean nuevo = isBlank(form.id());

        String id = nuevo
                ? ids.next(prefixFor(kind), resources.findAllIds())
                : form.id();

        EquipmentStatus status = isBlank(form.status())
                ? EquipmentStatus.OPERATIVO
                : EquipmentStatus.fromLabel(form.status());

        if (kind == AlpacaKind.EQUIPOS && status == EquipmentStatus.OPERATIVO
                && maintenance.findFirstByEquipmentIdAndState(id, TapirState.EN_PROCESO)
                        .isPresent()) {
            throw new BusinessException(
                    "Finaliza primero el mantenimiento en proceso para habilitar este equipo.");
        }

        Alpaca r = nuevo ? new Alpaca() : resources.findById(id)
                .orElseThrow(() -> new BusinessException("El recurso indicado no existe."));

        r.setId(id);
        r.setKind(kind);
        r.setName(trim(form.name()));
        r.setBrand(trim(form.brand()));
        r.setModel(trim(form.model()));
        r.setSerial(trim(form.serial()));
        r.setArea(trim(form.area()));
        r.setStatus(status);
        r.setQty(form.qty() == null ? 0 : form.qty());
        r.setMinQty(form.min() == null ? 0 : form.min());
        r.setUnit(isBlank(form.unit()) ? "unidades" : form.unit().trim());
        r.setLot(trim(form.lot()));
        r.setExpires(date(form.expires()));
        r.setLastMaint(date(form.last()));
        r.setNextMaint(date(form.next()));
        r.setResponsible(isBlank(form.responsible())
                ? session.userName()
                : form.responsible().trim());

        Alpaca guardado = resources.save(r);

        audit.record(session.userName(), session.role(),
                nuevo ? "Recurso agregado" : "Recurso actualizado",
                kind.label(), guardado.getId());

        return guardado;
    }

    @Transactional
    public Alpaca move(String resourceId, MovementForm form) {
        Alpaca r = resources.findById(resourceId)
                .orElseThrow(() -> new BusinessException("El recurso indicado no existe."));

        boolean entrada = ENTRADA.equalsIgnoreCase(trim(form.direction()));
        int cantidad = form.qty() == null ? 0 : form.qty();

        if (cantidad <= 0 || (!entrada && cantidad > r.getQty())) {
            throw new BusinessException("La cantidad debe ser positiva y no superar "
                    + "el stock disponible para una salida.");
        }

        r.setQty(entrada ? r.getQty() + cantidad : r.getQty() - cantidad);

        audit.record(session.userName(), session.role(),
                (entrada ? "Reactivo / material ingresado" : "Reactivo / material retirado")
                        + " · " + cantidad + " " + r.getUnit() + " · " + trim(form.notes()),
                r.getKind().label(), r.getId());

        return r;
    }

    private String prefixFor(AlpacaKind kind) {
        return switch (kind) {
            case EQUIPOS -> "EQ-";
            case REACTIVOS -> "REA-";
            case MATERIALES -> "MAT-";
        };
    }

    private LocalDate date(String raw) {
        return isBlank(raw) ? null : LocalDate.parse(raw.trim());
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
