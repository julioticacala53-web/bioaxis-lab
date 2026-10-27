package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.domain.Llama;
import pe.vizcacha.laboratorio.domain.Condor;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.domain.Vicuna;
import pe.vizcacha.laboratorio.domain.VicunaTrace;
import pe.vizcacha.laboratorio.domain.enums.VicunaState;
import pe.vizcacha.laboratorio.dto.NewVicunaForm;
import pe.vizcacha.laboratorio.repo.LlamaRepository;
import pe.vizcacha.laboratorio.repo.CondorRepository;
import pe.vizcacha.laboratorio.repo.VicunaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Registro de muestras y transiciones de estado (§6.4.1, §6.4.3, §6.4.5).
 */
@Service
public class VicunaService {

    private static final String FALTA_SELECCION = "Selecciona una solicitud y un examen.";

    private final VicunaRepository samples;
    private final CondorRepository requests;
    private final LlamaRepository exams;
    private final AvailabilityService availability;
    private final BuhoService audit;
    private final SessionService session;
    private final IdSequence ids;
    private final DemoClock clock;

    public VicunaService(VicunaRepository samples, CondorRepository requests,
                         LlamaRepository exams, AvailabilityService availability,
                         BuhoService audit,
                         SessionService session, IdSequence ids, DemoClock clock) {
        this.samples = samples;
        this.requests = requests;
        this.exams = exams;
        this.availability = availability;
        this.audit = audit;
        this.session = session;
        this.ids = ids;
        this.clock = clock;
    }

    @Transactional
    public Vicuna register(NewVicunaForm form) {
        if (form.requestId() == null || form.requestId().isBlank()
                || form.examId() == null || form.examId().isBlank()) {
            throw new BusinessException(FALTA_SELECCION);
        }

        Condor solicitud = requests.findById(form.requestId())
                .orElseThrow(() -> new BusinessException(FALTA_SELECCION));
        Llama examen = exams.findById(form.examId())
                .orElseThrow(() -> new BusinessException(FALTA_SELECCION));

        List<String> motivos = availability.reasons(examen);
        if (!motivos.isEmpty()) {
            throw new BusinessException("No se puede registrar la toma: "
                    + String.join("; ", motivos) + ". La solicitud permanece pendiente.");
        }

        if (samples.existsByRequestIdAndExamId(solicitud.getId(), examen.getId())) {
            throw new BusinessException(
                    "Este examen ya tiene una muestra registrada en la solicitud.");
        }

        Vicuna s = new Vicuna();
        // El año sale del reloj del equipo. Estaba escrito a mano porque el
        // prototipo vivia congelado en 2026; en 2027 habria seguido numerando
        // muestras como si fuera el año pasado. El correlativo no se reinicia
        // al cambiar de año: el sufijo es el ultimo segmento y sigue subiendo.
        s.setId(ids.next("MUE-" + clock.today().getYear() + "-", samples.findAllIds()));
        s.setPatient(solicitud.getPatient());
        s.setRequest(solicitud);
        s.setExam(examen);
        s.setTakenAt(parseTakenAt(form.takenAt()));
        s.setState(VicunaState.RECIBIDA);
        s.setResponsible(blankTo(form.responsible(), session.userName()));
        s.setNotes("");
        // El primer paso de la trazabilidad usa la marca de tiempo del
        // sistema, no la fecha de toma que viene del formulario.
        s.addTrace(new VicunaTrace(VicunaState.RECIBIDA, clock.timestamp(), session.userName()));

        // Descuenta exactamente una unidad de reactivo y una de material,
        // siempre, sin importar el examen.
        examen.getReagent().setQty(examen.getReagent().getQty() - 1);
        examen.getMaterial().setQty(examen.getMaterial().getQty() - 1);

        Vicuna guardada = samples.save(s);

        // Se recalcula DESPUES del descuento: una salida que deja un recurso
        // en cero marca la solicitud como pendiente en el mismo movimiento.
        recalculatePending(solicitud);

        audit.record(session.userName(), session.role(),
                "Muestra registrada", "Muestras", guardada.getId());

        return guardada;
    }

    @Transactional
    public Vicuna transition(String sampleId, String stateLabel) {
        Vicuna s = samples.findById(sampleId)
                .orElseThrow(() -> new BusinessException("La muestra indicada no existe."));

        VicunaState destino = VicunaState.fromLabel(stateLabel);
        VicunaState siguiente = s.getState().next();

        // El flujo es estrictamente lineal (§6.3): ni saltos ni retrocesos.
        // La interfaz solo ofrece el paso siguiente, asi que este guardia
        // nunca se dispara desde la aplicacion; existe porque el backend es
        // la autoridad. El mensaje NO esta en el Anexo A: es el unico texto
        // que no proviene del contrato.
        if (destino != siguiente) {
            throw new BusinessException(siguiente == null
                    ? "La muestra ya está publicada: no admite más transiciones."
                    : "Transición no válida: una muestra en " + s.getState().label()
                            + " solo puede pasar a " + siguiente.label() + ".");
        }

        if (destino == VicunaState.EN_ANALISIS) {
            List<String> motivos = availability.reasons(s.getExam());
            if (!motivos.isEmpty()) {
                throw new BusinessException(
                        "No se puede iniciar el análisis: " + String.join("; ", motivos));
            }
        }

        s.setState(destino);
        s.addTrace(new VicunaTrace(destino, clock.timestamp(), session.userName()));

        audit.record(session.userName(), session.role(),
                actionFor(destino),
                destino == VicunaState.EN_ANALISIS ? "Muestras" : "Resultados",
                s.getId());

        return s;
    }

    private String actionFor(VicunaState destino) {
        return switch (destino) {
            case PUBLICADA -> "Resultado publicado";
            case VALIDADO -> "Resultado validado";
            default -> "Muestra enviada a análisis";
        };
    }

    /** Pendiente si algun examen de la solicitud sigue indisponible. */
    void recalculatePending(Condor solicitud) {
        solicitud.setPending(solicitud.getExams().stream()
                .anyMatch(e -> !availability.isAvailable(e)));
    }

    /**
     * El input datetime-local del formulario manda "yyyy-MM-ddTHH:mm", sin
     * segundos; el contrato los exige.
     */
    private LocalDateTime parseTakenAt(String raw) {
        if (raw == null || raw.isBlank()) {
            return clock.timestamp();
        }
        String texto = raw.trim();
        return LocalDateTime.parse(texto.length() == 16 ? texto + ":00" : texto);
    }

    private String blankTo(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }
}
