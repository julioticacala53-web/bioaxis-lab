package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.domain.Vicuna;
import pe.vizcacha.laboratorio.domain.VicunaResult;
import pe.vizcacha.laboratorio.domain.VicunaTrace;
import pe.vizcacha.laboratorio.domain.enums.VicunaState;
import pe.vizcacha.laboratorio.dto.ResultForm;
import pe.vizcacha.laboratorio.repo.VicunaRepository;

import java.util.List;
import java.util.Map;

@Service
public class ResultService {

    private static final String SUFIJO_OBSERVACION = "_obs";

    private static final String MUESTRA_NO_VALIDA =
            "Selecciona una muestra en análisis para ingresar resultados.";

    private final VicunaRepository samples;
    private final ResultFormatCatalog formats;
    private final BuhoService audit;
    private final SessionService session;
    private final DemoClock clock;

    public ResultService(VicunaRepository samples, ResultFormatCatalog formats,
                         BuhoService audit,
                         SessionService session, DemoClock clock) {
        this.samples = samples;
        this.formats = formats;
        this.audit = audit;
        this.session = session;
        this.clock = clock;
    }

    @Transactional
    public Vicuna save(String sampleId, ResultForm form) {
        Vicuna s = samples.findById(sampleId)
                .orElseThrow(() -> new BusinessException(MUESTRA_NO_VALIDA));

        if (s.getState() != VicunaState.EN_ANALISIS) {
            throw new BusinessException(MUESTRA_NO_VALIDA);
        }

        Map<String, String> valores = form.values() == null ? Map.of() : form.values();
        List<LlamaParam> params = formats.params(s.getExam().getFormat());

        if (form.submit() && params.stream().anyMatch(p -> isBlank(valores.get(p.name())))) {
            throw new BusinessException(
                    "Completa todos los parámetros antes de enviar a validación.");
        }

        boolean numeroInvalido = params.stream().anyMatch(p ->
                p.isNumeric() && !isBlank(valores.get(p.name()))
                        && !isValidNonNegative(valores.get(p.name())));
        if (numeroInvalido) {
            throw new BusinessException(
                    "Los parámetros numéricos deben contener valores válidos, no negativos.");
        }

        java.util.Set<String> vigentes = new java.util.HashSet<>();
        for (LlamaParam p : params) {
            vigentes.add(p.name());
        }
        s.getResults().removeIf(r -> !vigentes.contains(r.getParamName()));

        for (LlamaParam p : params) {
            String valor = valores.get(p.name());
            String observacion = valores.get(p.name() + SUFIJO_OBSERVACION);
            VicunaResult r = s.getResults().stream()
                    .filter(x -> x.getParamName().equals(p.name()))
                    .findFirst()
                    .orElse(null);
            if (r == null) {
                r = new VicunaResult();
                r.setParamName(p.name());
                r.setSample(s);
                s.getResults().add(r);
            }
            r.setValue(valor == null ? "" : valor.trim());
            r.setObservation(isBlank(observacion) ? null : observacion.trim());
        }

        s.setNotes(form.notes() == null ? "" : form.notes());

        if (form.submit()) {
            s.setState(VicunaState.EN_VALIDACION);
            s.addTrace(new VicunaTrace(VicunaState.EN_VALIDACION,
                    clock.timestamp(), session.userName()));
        }

        audit.record(session.userName(), session.role(),
                form.submit() ? "Resultado ingresado" : "Borrador de resultado guardado",
                "Resultados", s.getId());

        return s;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean isValidNonNegative(String raw) {
        try {
            double v = Double.parseDouble(raw.trim());
            return Double.isFinite(v) && v >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
