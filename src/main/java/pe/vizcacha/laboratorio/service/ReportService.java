package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.domain.Buho;
import pe.vizcacha.laboratorio.domain.Llama;
import pe.vizcacha.laboratorio.domain.Condor;
import pe.vizcacha.laboratorio.domain.Tapir;
import pe.vizcacha.laboratorio.domain.Puma;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.domain.Vicuna;
import pe.vizcacha.laboratorio.domain.enums.TapirState;
import pe.vizcacha.laboratorio.domain.enums.AlpacaKind;
import pe.vizcacha.laboratorio.domain.enums.VicunaState;
import pe.vizcacha.laboratorio.dto.ReportDto;
import pe.vizcacha.laboratorio.repo.BuhoRepository;
import pe.vizcacha.laboratorio.repo.LlamaRepository;
import pe.vizcacha.laboratorio.repo.CondorRepository;
import pe.vizcacha.laboratorio.repo.TapirRepository;
import pe.vizcacha.laboratorio.repo.PumaRepository;
import pe.vizcacha.laboratorio.repo.AlpacaRepository;
import pe.vizcacha.laboratorio.repo.VicunaRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ReportService {

    public static final String ACTIVIDAD = "Actividad del laboratorio";
    public static final String MAS_SOLICITADOS = "Exámenes más solicitados";
    public static final String INVENTARIO = "Inventario";
    public static final String EN_MANTENIMIENTO = "Equipos en mantenimiento";
    public static final String CONSUMO_REACTIVOS = "Consumo de reactivos";
    public static final String CONSUMO_MATERIALES = "Consumo de materiales";

    public static final String HOY = "Hoy";
    public static final String ESTA_SEMANA = "Esta semana";
    public static final String ESTE_MES = "Este mes";
    public static final String PERSONALIZADO = "Rango personalizado";

    private static final Pattern CONSUMO = Pattern.compile("retirado|ajustado|físico");

    private static final DateTimeFormatter FECHA_ES_PE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final PumaRepository patients;
    private final CondorRepository requests;
    private final VicunaRepository samples;
    private final AlpacaRepository resources;
    private final LlamaRepository exams;
    private final TapirRepository maintenance;
    private final BuhoRepository audit;
    private final AlpacaStatusService statuses;
    private final DemoClock clock;

    public ReportService(PumaRepository patients, CondorRepository requests,
                         VicunaRepository samples, AlpacaRepository resources,
                         LlamaRepository exams, TapirRepository maintenance,
                         BuhoRepository audit, AlpacaStatusService statuses,
                         DemoClock clock) {
        this.patients = patients;
        this.requests = requests;
        this.samples = samples;
        this.resources = resources;
        this.exams = exams;
        this.maintenance = maintenance;
        this.audit = audit;
        this.statuses = statuses;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public ReportDto build(String type, String range, String customFrom, String customTo) {
        String tipo = type == null || type.isBlank() ? ACTIVIDAD : type;
        String rango = range == null || range.isBlank() ? HOY : range;

        LocalDate desde = clock.today();
        LocalDate hasta = clock.today();
        switch (rango) {

            case ESTA_SEMANA -> desde = clock.today().with(DayOfWeek.MONDAY);
            case ESTE_MES -> desde = clock.today().withDayOfMonth(1);
            case PERSONALIZADO -> {
                desde = parse(customFrom, clock.today());
                hasta = parse(customTo, clock.today());
            }
            default -> {  }
        }

        List<String> columns;
        List<List<String>> rows;

        switch (tipo) {
            case MAS_SOLICITADOS -> {
                columns = List.of("Examen", "Categoría", "Solicitudes");
                rows = masSolicitados(desde, hasta);
            }
            case INVENTARIO -> {
                columns = List.of("Recurso", "Tipo", "Stock actual", "Estado");
                rows = inventario();
            }
            case EN_MANTENIMIENTO -> {
                columns = List.of("Equipo", "Responsable", "Finalización estimada", "Estado");
                rows = equiposEnMantenimiento();
            }
            case CONSUMO_REACTIVOS -> {
                columns = List.of("Fecha", "Recurso", "Movimiento", "Responsable");
                rows = consumo(AlpacaKind.REACTIVOS, desde, hasta);
            }
            case CONSUMO_MATERIALES -> {
                columns = List.of("Fecha", "Recurso", "Movimiento", "Responsable");
                rows = consumo(AlpacaKind.MATERIALES, desde, hasta);
            }
            default -> {
                columns = List.of("Indicador", "Cantidad", "Período");
                rows = actividad(rango, desde, hasta);
            }
        }

        return new ReportDto(tipo, rango, desde.toString(), hasta.toString(), columns, rows);
    }

    private List<List<String>> actividad(String rango, LocalDate desde, LocalDate hasta) {
        List<Vicuna> ss = samples.findAll().stream()
                .filter(s -> inRange(s.getTakenAt().toLocalDate(), desde, hasta))
                .toList();
        long rr = requests.findAll().stream()
                .filter(r -> inRange(r.getDate(), desde, hasta))
                .count();
        long nuevos = patients.findAll().stream()
                .filter(p -> inRange(p.getCreated(), desde, hasta))
                .count();

        long realizados = ss.stream().filter(s ->
                s.getState() == VicunaState.EN_VALIDACION
                        || s.getState() == VicunaState.VALIDADO
                        || s.getState() == VicunaState.PUBLICADA).count();
        long procesadas = ss.stream().filter(s -> s.getState() != VicunaState.RECIBIDA).count();
        long publicados = ss.stream().filter(s -> s.getState() == VicunaState.PUBLICADA).count();
        long pendientes = ss.size() - publicados;

        return List.of(
                List.of("Pacientes registrados", String.valueOf(nuevos), rango),
                List.of("Solicitudes registradas", String.valueOf(rr), rango),
                List.of("Exámenes realizados", String.valueOf(realizados), rango),
                List.of("Muestras procesadas", String.valueOf(procesadas), rango),
                List.of("Resultados publicados", String.valueOf(publicados), rango),
                List.of("Resultados pendientes", String.valueOf(pendientes), rango));
    }

    private List<List<String>> masSolicitados(LocalDate desde, LocalDate hasta) {
        List<Condor> enRango = requests.findAll().stream()
                .filter(r -> inRange(r.getDate(), desde, hasta))
                .toList();

        List<List<String>> filas = new ArrayList<>();
        for (Llama e : exams.findAll().stream().sorted(Comparator.comparing(Llama::getId)).toList()) {
            long cuantas = enRango.stream()
                    .filter(r -> r.getExams().stream().anyMatch(x -> x.getId().equals(e.getId())))
                    .count();
            filas.add(List.of(e.getName(), e.getCategory(), String.valueOf(cuantas)));
        }
        return filas;
    }

    private List<List<String>> inventario() {
        return resources.findAll().stream()
                .filter(r -> r.getKind() != AlpacaKind.EQUIPOS)
                .sorted(Comparator
                        .comparingInt((Alpaca r) -> r.getKind() == AlpacaKind.REACTIVOS ? 0 : 1)
                        .thenComparing(Alpaca::getId))
                .map(r -> List.of(r.getName(), r.getKind().label(),
                        r.getQty() + " " + r.getUnit(), statuses.derived(r)))
                .toList();
    }

    private List<List<String>> equiposEnMantenimiento() {
        return maintenance.findAll().stream()
                .filter(m -> m.getState() == TapirState.EN_PROCESO)
                .sorted(Comparator.comparing(Tapir::getId))
                .map(m -> List.of(m.getEquipment().getName(), m.getResponsible(),
                        m.getEndDate().format(FECHA_ES_PE), m.getState().label()))
                .toList();
    }

    private List<List<String>> consumo(AlpacaKind kind, LocalDate desde, LocalDate hasta) {
        return audit.findAllByOrderBySeqAsc().stream()
                .filter(a -> inRange(a.getAt().toLocalDate(), desde, hasta))
                .filter(a -> CONSUMO.matcher(a.getAction()).find())
                .filter(a -> a.getModule().equals(kind.label())
                        || a.getModule().equals("Inventario"))
                .map(a -> List.of(a.getAt().toLocalDate().format(FECHA_ES_PE),
                        a.getRecordRef(), a.getAction(), a.getUserName()))
                .toList();
    }

    private boolean inRange(LocalDate d, LocalDate desde, LocalDate hasta) {
        return d != null && !d.isBefore(desde) && !d.isAfter(hasta);
    }

    private LocalDate parse(String raw, LocalDate fallback) {
        return raw == null || raw.isBlank() ? fallback : LocalDate.parse(raw.trim());
    }
}
