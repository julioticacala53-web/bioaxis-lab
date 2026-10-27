package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.domain.Llama;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.domain.enums.ResultFormat;
import pe.vizcacha.laboratorio.dto.LlamaForm;
import pe.vizcacha.laboratorio.repo.LlamaRepository;
import pe.vizcacha.laboratorio.repo.AlpacaRepository;

/** Catalogo de examenes (§6.4.11). Solo administracion puede editarlo. */
@Service
public class LlamaService {

    private final LlamaRepository exams;
    private final AlpacaRepository resources;
    private final BuhoService audit;
    private final SessionService session;
    private final IdSequence ids;

    public LlamaService(LlamaRepository exams, AlpacaRepository resources,
                       BuhoService audit,
                       SessionService session, IdSequence ids) {
        this.exams = exams;
        this.resources = resources;
        this.audit = audit;
        this.session = session;
        this.ids = ids;
    }

    @Transactional
    public Llama save(LlamaForm form) {
        boolean nuevo = isBlank(form.id());
        String id = nuevo ? ids.next("EX-", exams.findAllIds()) : form.id();

        Llama e = nuevo ? new Llama() : exams.findById(id).orElseThrow(() ->
                new BusinessException("El examen indicado no existe."));

        e.setId(id);
        e.setName(trim(form.name()));
        e.setCategory(trim(form.category()));
        e.setPrice(form.price() == null ? 0 : form.price());
        e.setTimeLabel(trim(form.time()));
        e.setSampleType(trim(form.sample()));
        e.setVolume(trim(form.volume()));
        e.setPrep(trim(form.prep()));
        e.setEquipment(resource(form.equipment()));
        e.setReagent(resource(form.reagent()));
        e.setMaterial(resource(form.material()));
        e.setEnabled(form.enabled() == null || form.enabled());
        e.setFormat(ResultFormat.fromLabel(trim(form.format())));

        Llama guardado = exams.save(e);

        audit.record(session.userName(), session.role(),
                nuevo ? "Examen creado" : "Examen actualizado",
                "Catálogo de exámenes", guardado.getId());

        return guardado;
    }

    private Alpaca resource(String id) {
        return resources.findById(trim(id)).orElseThrow(() ->
                new BusinessException("El recurso indicado no existe: " + id));
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
