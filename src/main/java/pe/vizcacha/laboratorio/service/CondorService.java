package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.domain.Llama;
import pe.vizcacha.laboratorio.domain.Condor;
import pe.vizcacha.laboratorio.domain.Puma;
import pe.vizcacha.laboratorio.dto.NewCondorForm;
import pe.vizcacha.laboratorio.repo.LlamaRepository;
import pe.vizcacha.laboratorio.repo.CondorRepository;
import pe.vizcacha.laboratorio.repo.PumaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CondorService {

    private static final String FALTA_SELECCION = "Selecciona un paciente y al menos un examen.";

    private final CondorRepository requests;
    private final PumaRepository patients;
    private final LlamaRepository exams;
    private final AvailabilityService availability;
    private final BuhoService audit;
    private final SessionService session;
    private final IdSequence ids;
    private final DemoClock clock;

    public CondorService(CondorRepository requests, PumaRepository patients,
                          LlamaRepository exams, AvailabilityService availability,
                          BuhoService audit, SessionService session,
                          IdSequence ids, DemoClock clock) {
        this.requests = requests;
        this.patients = patients;
        this.exams = exams;
        this.availability = availability;
        this.audit = audit;
        this.session = session;
        this.ids = ids;
        this.clock = clock;
    }

    @Transactional
    public Condor create(NewCondorForm form) {
        if (form.patientId() == null || form.patientId().isBlank()
                || form.examIds() == null || form.examIds().isEmpty()) {
            throw new BusinessException(FALTA_SELECCION);
        }

        Puma paciente = patients.findById(form.patientId())
                .orElseThrow(() -> new BusinessException(FALTA_SELECCION));

        List<Llama> elegidos = new ArrayList<>();
        for (String examId : form.examIds()) {
            elegidos.add(exams.findById(examId)
                    .orElseThrow(() -> new BusinessException(FALTA_SELECCION)));
        }

        boolean pendiente = elegidos.stream().anyMatch(e -> !availability.isAvailable(e));

        if (pendiente && !form.allowPending()) {
            throw new BusinessException(
                    "Confirma el registro pendiente de disponibilidad para continuar.");
        }

        Condor r = new Condor();
        r.setId(ids.next("SOL-", requests.findAllIds()));
        r.setPatient(paciente);
        r.setDate(clock.today());
        r.setPending(pendiente);
        r.setExams(elegidos);

        Condor guardada = requests.save(r);

        audit.record(session.userName(), session.role(),
                "Solicitud creada", "Solicitudes", guardada.getId());

        return guardada;
    }
}
