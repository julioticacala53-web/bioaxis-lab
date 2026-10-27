package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.domain.Vicuna;
import pe.vizcacha.laboratorio.domain.VicunaResult;
import pe.vizcacha.laboratorio.domain.VicunaTrace;
import pe.vizcacha.laboratorio.domain.Settings;
import pe.vizcacha.laboratorio.domain.enums.VicunaState;
import pe.vizcacha.laboratorio.dto.ResultReportDto;
import pe.vizcacha.laboratorio.repo.VicunaRepository;
import pe.vizcacha.laboratorio.repo.SettingsRepository;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResultReportService {

    private static final String AVISO =
            "Este documento no constituye un informe clínico ni un diagnóstico médico. "
                    + "Los valores fuera del intervalo de referencia solo señalan una desviación.";

    private static final DateTimeFormatter FECHA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy · HH:mm");

    private static final DateTimeFormatter SOLO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final VicunaRepository samples;
    private final SettingsRepository settings;
    private final ResultFormatCatalog formats;
    private final DemoClock clock;
    private final SessionService session;

    public ResultReportService(VicunaRepository samples, SettingsRepository settings,
                               ResultFormatCatalog formats, DemoClock clock,
                               SessionService session) {
        this.session = session;
        this.samples = samples;
        this.settings = settings;
        this.formats = formats;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public ResultReportDto build(String sampleId) {
        Vicuna s = samples.findById(sampleId)
                .orElseThrow(() -> new BusinessException("La muestra indicada no existe."));

        if (s.getState() != VicunaState.PUBLICADA) {
            throw new BusinessException(
                    "Solo se puede imprimir un resultado publicado.");
        }

        Settings config = settings.findById(Settings.SINGLETON_ID).orElse(null);

        Map<String, VicunaResult> porParametro = new LinkedHashMap<>();
        for (VicunaResult r : s.getResults()) {
            porParametro.put(r.getParamName(), r);
        }

        List<ResultReportDto.Row> filas = new ArrayList<>();
        List<String> fuera = new ArrayList<>();

        for (LlamaParam p : formats.params(s.getExam().getFormat())) {
            VicunaResult r = porParametro.get(p.name());
            String valor = r == null || r.getValue() == null ? "" : r.getValue();
            String marca = flagFor(p, valor);
            boolean desviado = !marca.isEmpty();
            if (desviado) {
                fuera.add(p.name());
            }
            filas.add(new ResultReportDto.Row(
                    p.name(),
                    valor.isBlank() ? "—" : valor,
                    p.unit(),
                    p.reference(),
                    r == null || r.getObservation() == null || r.getObservation().isBlank()
                            ? "—" : r.getObservation(),
                    desviado,
                    marca));
        }

        return new ResultReportDto(
                config == null ? SettingsService.LAB_POR_DEFECTO : config.getLab(),
                config == null ? "Laboratorio Central" : config.getBranch(),
                s.getId(),
                clock.timestamp().format(FECHA_HORA),
                s.getPatient().fullName(),
                s.getPatient().getDni(),
                s.getPatient().getBirth().format(SOLO_FECHA),
                s.getPatient().age(clock.today()),
                s.getPatient().getSex(),
                s.getId(),
                s.getExam().getName(),
                s.getExam().getCategory(),
                s.getExam().getSampleType(),
                s.getTakenAt().format(FECHA_HORA),
                firmaFecha(s, VicunaState.PUBLICADA),
                firmaUsuario(s, VicunaState.EN_VALIDACION),
                firmaUsuario(s, VicunaState.VALIDADO),
                firmaUsuario(s, VicunaState.PUBLICADA),
                filas,
                s.getNotes() == null || s.getNotes().isBlank() ? "—" : s.getNotes(),
                fuera,
                AVISO);
    }

    private String flagFor(LlamaParam p, String valor) {
        if (!p.isNumeric() || valor == null || valor.isBlank()) {
            return "";
        }
        try {
            double v = Double.parseDouble(valor.trim());
            if (v > p.max()) {
                return "ALTO";
            }
            if (v < p.min()) {
                return "BAJO";
            }
            return "";
        } catch (NumberFormatException e) {
            return "";
        }
    }

    private String firmaUsuario(Vicuna s, VicunaState estado) {
        return traza(s, estado).map(VicunaTrace::getUserName)
                .map(n -> SessionService.REGISTRO_LOCAL.equals(n) ? session.userName() : n)
                .orElse("—");
    }

    private String firmaFecha(Vicuna s, VicunaState estado) {
        return traza(s, estado).map(t -> t.getAt().format(FECHA_HORA)).orElse("—");
    }

    private java.util.Optional<VicunaTrace> traza(Vicuna s, VicunaState estado) {
        return s.getTraces().stream().filter(t -> t.getState() == estado).findFirst();
    }
}
