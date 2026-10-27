package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.domain.Llama;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.domain.enums.EquipmentStatus;
import pe.vizcacha.laboratorio.domain.enums.AlpacaKind;
import pe.vizcacha.laboratorio.repo.LlamaRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Disponibilidad de un examen (§6.1). La regla central de todo el sistema.
 *
 * Devuelve la lista de motivos por los que un examen no puede procesarse;
 * vacia significa disponible. Cada examen depende de exactamente un equipo,
 * un reactivo y un material.
 *
 * Dos umbrales que es facil confundir y que aqui importan:
 *
 *  - Bloquea con qty < 1, es decir solo con stock en CERO. "Stock bajo"
 *    (qty <= min) genera alerta pero no bloquea nada. En la semilla el
 *    reactivo de glucosa tiene 12 unidades con minimo 20: esta en stock bajo
 *    y el examen sigue disponible.
 *  - El vencimiento solo se evalua en reactivos, y vencer HOY no cuenta como
 *    vencido: la comparacion es estrictamente anterior.
 */
@Service
public class AvailabilityService {

    private final LlamaRepository exams;
    private final DemoClock clock;

    public AvailabilityService(LlamaRepository exams, DemoClock clock) {
        this.exams = exams;
        this.clock = clock;
    }

    public List<String> reasons(Llama exam) {
        List<String> motivos = new ArrayList<>();

        if (!exam.isEnabled()) {
            motivos.add("Examen deshabilitado");
        }

        Alpaca equipo = exam.getEquipment();
        if (equipo == null || equipo.getStatus() != EquipmentStatus.OPERATIVO) {
            String nombre = equipo == null ? "Equipo" : equipo.getName();
            boolean enMantenimiento = equipo != null
                    && equipo.getStatus() == EquipmentStatus.EN_MANTENIMIENTO;
            motivos.add(nombre + ": "
                    + (enMantenimiento ? "equipo en mantenimiento" : "equipo no operativo"));
        }

        for (Alpaca r : List.of(exam.getReagent(), exam.getMaterial())) {
            motivos.addAll(reasonsFor(r));
        }

        return motivos;
    }

    private List<String> reasonsFor(Alpaca r) {
        if (r == null) {
            return List.of("Recurso no configurado");
        }
        if (r.getQty() < 1) {
            return List.of(r.getName() + ": " + (r.getKind() == AlpacaKind.REACTIVOS
                    ? "reactivo sin stock"
                    : "material insuficiente"));
        }
        if (r.getKind() == AlpacaKind.REACTIVOS
                && r.getExpires() != null
                && r.getExpires().isBefore(clock.today())) {
            return List.of(r.getName() + ": producto vencido");
        }
        return List.of();
    }

    public boolean isAvailable(Llama exam) {
        return reasons(exam).isEmpty();
    }

    /** Lo que muestra el panel Estado del laboratorio: 4 de 8 con la semilla. */
    @Transactional(readOnly = true)
    public List<Llama> unavailableExams() {
        return exams.findAll().stream().filter(e -> !isAvailable(e)).toList();
    }
}
