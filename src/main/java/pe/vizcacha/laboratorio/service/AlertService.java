package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.domain.Llama;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.domain.enums.EquipmentStatus;
import pe.vizcacha.laboratorio.domain.enums.AlpacaKind;
import pe.vizcacha.laboratorio.domain.enums.VicunaState;
import pe.vizcacha.laboratorio.dto.AlertDto;
import pe.vizcacha.laboratorio.repo.LlamaRepository;
import pe.vizcacha.laboratorio.repo.AlpacaRepository;
import pe.vizcacha.laboratorio.repo.VicunaRepository;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Alertas operativas (§6.6). Se calculan en caliente desde el estado, nunca
 * se almacenan.
 *
 * El orden del recorrido es parte del contrato porque el dashboard muestra
 * solo las 4 primeras. Un mismo reactivo puede generar DOS alertas -- stock
 * bajo y proximo a vencer -- y eso es intencional.
 *
 * Con la semilla salen exactamente 8.
 */
@Service
public class AlertService {

    private static final int DIAS_DE_AVISO = 30;

    private static final DateTimeFormatter FECHA_ES_PE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final AlpacaRepository resources;
    private final LlamaRepository exams;
    private final VicunaRepository samples;
    private final AvailabilityService availability;
    private final DemoClock clock;

    public AlertService(AlpacaRepository resources, LlamaRepository exams,
                        VicunaRepository samples, AvailabilityService availability,
                        DemoClock clock) {
        this.resources = resources;
        this.exams = exams;
        this.samples = samples;
        this.availability = availability;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public List<AlertDto> alerts() {
        List<AlertDto> list = new ArrayList<>();

        for (Alpaca r : resourcesInSeedOrder()) {
            if (r.getKind() == AlpacaKind.EQUIPOS
                    && r.getStatus() == EquipmentStatus.EN_MANTENIMIENTO) {
                list.add(new AlertDto(r.getName(),
                        "En mantenimiento · disponibilidad afectada",
                        "Equipos", "amber", "monitor"));

            } else if (r.getKind() != AlpacaKind.EQUIPOS && r.getQty() <= r.getMinQty()) {
                boolean sinNada = r.getQty() == 0;
                list.add(new AlertDto(r.getName(),
                        sinNada
                                ? "Sin existencias · requiere reposición"
                                : "Stock bajo · " + r.getQty() + " " + r.getUnit() + " disponibles",
                        r.getKind().label(),
                        sinNada ? "red" : "amber",
                        r.getKind() == AlpacaKind.REACTIVOS ? "flask" : "box"));
            }

            // Independiente de la anterior: un reactivo con stock bajo y
            // proximo a vencer produce las dos.
            if (r.getKind() == AlpacaKind.REACTIVOS && r.getExpires() != null) {
                long dias = ChronoUnit.DAYS.between(clock.today(), r.getExpires());
                if (dias <= DIAS_DE_AVISO) {
                    list.add(new AlertDto(r.getName(),
                            dias < 0
                                    ? "Producto vencido"
                                    : "Vence el " + r.getExpires().format(FECHA_ES_PE)
                                            + " · " + dias + " días",
                            "Reactivos",
                            dias < 0 ? "red" : "amber",
                            "calendar"));
                }
            }
        }

        long porValidar = samples.countByState(VicunaState.EN_VALIDACION);
        if (porValidar > 0) {
            list.add(new AlertDto("Resultados por validar",
                    porValidar + " resultados requieren revisión",
                    "Resultados", "blue", "file"));
        }

        long pendientes = samples.countByState(VicunaState.RECIBIDA);
        if (pendientes > 0) {
            list.add(new AlertDto("Muestras pendientes",
                    pendientes + " muestras por procesar",
                    "Muestras", "amber", "tube"));
        }

        List<Llama> noDisponibles = exams.findAll().stream()
                .filter(e -> !availability.isAvailable(e))
                .toList();
        if (!noDisponibles.isEmpty()) {
            list.add(new AlertDto("Exámenes no disponibles",
                    noDisponibles.size() + " exámenes con recursos afectados",
                    "Catálogo de exámenes", "red", "book"));
        }

        return list;
    }

    /** Mismo orden que el array del original: equipos, reactivos, materiales. */
    private List<Alpaca> resourcesInSeedOrder() {
        return resources.findAll().stream()
                .sorted(Comparator
                        .comparingInt((Alpaca r) -> switch (r.getKind()) {
                            case EQUIPOS -> 0;
                            case REACTIVOS -> 1;
                            case MATERIALES -> 2;
                        })
                        .thenComparing(Alpaca::getId))
                .toList();
    }
}
