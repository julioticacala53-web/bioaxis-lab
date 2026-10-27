package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.domain.Buho;
import pe.vizcacha.laboratorio.domain.Llama;
import pe.vizcacha.laboratorio.domain.Condor;
import pe.vizcacha.laboratorio.domain.Tapir;
import pe.vizcacha.laboratorio.domain.Puma;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.domain.Vicuna;
import pe.vizcacha.laboratorio.domain.VicunaResult;
import pe.vizcacha.laboratorio.domain.VicunaTrace;
import pe.vizcacha.laboratorio.domain.Preference;
import pe.vizcacha.laboratorio.domain.Settings;
import pe.vizcacha.laboratorio.domain.enums.AlpacaKind;
import pe.vizcacha.laboratorio.dto.BuhoDto;
import pe.vizcacha.laboratorio.dto.DatabaseDto;
import pe.vizcacha.laboratorio.dto.LlamaDto;
import pe.vizcacha.laboratorio.dto.TapirDto;
import pe.vizcacha.laboratorio.dto.PumaDto;
import pe.vizcacha.laboratorio.dto.CondorDto;
import pe.vizcacha.laboratorio.dto.AlpacaDto;
import pe.vizcacha.laboratorio.dto.VicunaDto;
import pe.vizcacha.laboratorio.dto.TraceDto;
import pe.vizcacha.laboratorio.repo.BuhoRepository;
import pe.vizcacha.laboratorio.repo.LlamaRepository;
import pe.vizcacha.laboratorio.repo.CondorRepository;
import pe.vizcacha.laboratorio.repo.TapirRepository;
import pe.vizcacha.laboratorio.repo.PumaRepository;
import pe.vizcacha.laboratorio.repo.AlpacaRepository;
import pe.vizcacha.laboratorio.repo.VicunaRepository;
import pe.vizcacha.laboratorio.repo.SettingsRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseSnapshotService {

    private static final DateTimeFormatter ISO_SEGUNDOS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private static final String SUFIJO_OBSERVACION = "_obs";

    private final PumaRepository patients;
    private final LlamaRepository exams;
    private final AlpacaRepository resources;
    private final CondorRepository requests;
    private final VicunaRepository samples;
    private final TapirRepository maintenance;
    private final BuhoRepository audit;
    private final SettingsRepository settings;
    private final AvailabilityService availability;
    private final AlpacaStatusService statuses;
    private final AlertService alerts;
    private final ResultFormatCatalog formats;
    private final ExportService exports;
    private final PreferenceService preferences;
    private final SessionService session;

    public DatabaseSnapshotService(PumaRepository patients, LlamaRepository exams,
                                   AlpacaRepository resources, CondorRepository requests,
                                   VicunaRepository samples, TapirRepository maintenance,
                                   BuhoRepository audit,
                                   SettingsRepository settings,
                                   AvailabilityService availability,
                                   AlpacaStatusService statuses, AlertService alerts,
                                   ResultFormatCatalog formats,
                                   ExportService exports, PreferenceService preferences,
                                   SessionService session) {
        this.session = session;
        this.patients = patients;
        this.exams = exams;
        this.resources = resources;
        this.requests = requests;
        this.samples = samples;
        this.maintenance = maintenance;
        this.audit = audit;
        this.settings = settings;
        this.availability = availability;
        this.statuses = statuses;
        this.alerts = alerts;
        this.formats = formats;
        this.exports = exports;
        this.preferences = preferences;
    }

    @Transactional(readOnly = true)
    public DatabaseDto snapshot() {
        Settings config = settings.findById(Settings.SINGLETON_ID).orElse(null);

        return new DatabaseDto(
                patients.findAll().stream().sorted(byId(Puma::getId)).map(this::toDto).toList(),
                exams.findAll().stream().sorted(byId(Llama::getId)).map(this::toDto).toList(),
                resourcesInSeedOrder(),
                requests.findAll().stream().sorted(byId(Condor::getId)).map(this::toDto).toList(),
                samples.findAll().stream().sorted(byId(Vicuna::getId)).map(this::toDto).toList(),
                maintenance.findAll().stream().sorted(byId(Tapir::getId)).map(this::toDto).toList(),
                audit.findAllByOrderBySeqAsc().stream().map(this::toDto).toList(),
                alerts.alerts(),
                formatsByLabel(),
                config == null ? SettingsService.LAB_POR_DEFECTO : config.getLab(),
                config == null ? "Laboratorio Central" : config.getBranch(),
                config == null || config.isNotifications(),
                Boolean.getBoolean("laboratorio.ventana"),
                exports.carpeta().toAbsolutePath().toString(),
                preferences.get(Preference.APARIENCIA, Preference.APARIENCIA_POR_DEFECTO),
                session.userName());
    }

    private java.util.Map<String, List<pe.vizcacha.laboratorio.service.LlamaParam>> formatsByLabel() {
        java.util.Map<String, List<pe.vizcacha.laboratorio.service.LlamaParam>> porEtiqueta =
                new java.util.LinkedHashMap<>();
        for (pe.vizcacha.laboratorio.domain.enums.ResultFormat f
                : pe.vizcacha.laboratorio.domain.enums.ResultFormat.values()) {
            porEtiqueta.put(f.label(), formats.params(f));
        }
        return porEtiqueta;
    }

    private <T> Comparator<T> byId(java.util.function.Function<T, String> id) {
        return Comparator.comparing(id);
    }

    private List<AlpacaDto> resourcesInSeedOrder() {
        Comparator<Alpaca> orden = Comparator
                .comparingInt((Alpaca r) -> switch (r.getKind()) {
                    case EQUIPOS -> 0;
                    case REACTIVOS -> 1;
                    case MATERIALES -> 2;
                })
                .thenComparing(Alpaca::getId);

        return resources.findAll().stream().sorted(orden).map(this::toDto).toList();
    }

    private PumaDto toDto(Puma p) {
        return new PumaDto(p.getId(), p.getDni(), p.getFirstName(), p.getLastName(),
                iso(p.getBirth()), p.getSex(), nz(p.getPhone()), nz(p.getEmail()),
                nz(p.getAddress()), nz(p.getNotes()), iso(p.getCreated()));
    }

    private LlamaDto toDto(Llama e) {
        List<String> motivos = availability.reasons(e);
        return new LlamaDto(e.getId(), e.getName(), e.getCategory(), e.getPrice(),
                e.getTimeLabel(), e.getSampleType(), e.getVolume(), nz(e.getPrep()),
                e.getEquipment().getId(), e.getReagent().getId(), e.getMaterial().getId(),
                e.isEnabled(), e.getFormat().label(),
                motivos.isEmpty(), motivos);
    }

    private AlpacaDto toDto(Alpaca r) {
        return new AlpacaDto(r.getId(), r.getName(), r.getKind().label(),
                nz(r.getBrand()), nz(r.getModel()), nz(r.getSerial()), r.getArea(),
                r.getStatus() == null ? "" : r.getStatus().label(),
                r.getQty(), r.getMinQty(), r.getUnit(), nz(r.getLot()),
                iso(r.getExpires()), iso(r.getLastMaint()), iso(r.getNextMaint()),
                nz(r.getResponsible()), statuses.derived(r));
    }

    private CondorDto toDto(Condor r) {
        return new CondorDto(r.getId(), r.getPatient().getId(),
                r.getExams().stream().map(Llama::getId).toList(),
                iso(r.getDate()), r.isPending());
    }

    private VicunaDto toDto(Vicuna s) {
        return new VicunaDto(s.getId(), s.getPatient().getId(), s.getRequest().getId(),
                s.getExam().getId(), iso(s.getTakenAt()), s.getState().label(),
                s.getResponsible(), flattenResults(s), nz(s.getNotes()),
                s.getTraces().stream().map(this::toDto).toList());
    }

    private Map<String, String> flattenResults(Vicuna s) {
        Map<String, String> plano = new LinkedHashMap<>();
        for (VicunaResult r : s.getResults()) {
            plano.put(r.getParamName(), nz(r.getValue()));
            if (r.getObservation() != null && !r.getObservation().isBlank()) {
                plano.put(r.getParamName() + SUFIJO_OBSERVACION, r.getObservation());
            }
        }
        return plano;
    }

    private TraceDto toDto(VicunaTrace t) {
        return new TraceDto(t.getState().label(), iso(t.getAt()), t.getUserName());
    }

    private TapirDto toDto(Tapir m) {
        return new TapirDto(m.getId(), m.getEquipment().getId(), m.getType(),
                iso(m.getStartDate()), iso(m.getEndDate()), iso(m.getActualDate()),
                m.getResponsible(), nz(m.getProvider()), nz(m.getDescription()),
                m.getState().label());
    }

    private BuhoDto toDto(Buho a) {
        return new BuhoDto(a.getId(), iso(a.getAt()), a.getUserName(), a.getRole().label(),
                a.getAction(), a.getModule(), a.getRecordRef());
    }

    private String iso(LocalDate d) {
        return d == null ? "" : d.toString();
    }

    private String iso(LocalDateTime d) {
        return d == null ? "" : d.format(ISO_SEGUNDOS);
    }

    private String nz(String s) {
        return s == null ? "" : s;
    }
}
