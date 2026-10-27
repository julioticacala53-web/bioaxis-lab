package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.domain.Puma;
import pe.vizcacha.laboratorio.dto.PumaForm;
import pe.vizcacha.laboratorio.repo.PumaRepository;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Service
public class PumaService {

    private static final Pattern OCHO_DIGITOS = Pattern.compile("\\d{8}");

    private final PumaRepository patients;
    private final BuhoService audit;
    private final SessionService session;
    private final IdSequence ids;
    private final DemoClock clock;

    public PumaService(PumaRepository patients, BuhoService audit,
                          SessionService session, IdSequence ids, DemoClock clock) {
        this.patients = patients;
        this.audit = audit;
        this.session = session;
        this.ids = ids;
        this.clock = clock;
    }

    @Transactional
    public Puma save(PumaForm form) {
        String dni = trim(form.dni());
        if (!OCHO_DIGITOS.matcher(dni).matches()) {
            throw new BusinessException("El DNI debe tener exactamente 8 dígitos.");
        }

        boolean nuevo = isBlank(form.id());

        patients.findByDni(dni)
                .filter(otro -> nuevo || !otro.getId().equals(form.id()))
                .ifPresent(otro -> {
                    throw new BusinessException(
                            "Ya existe un paciente con este DNI. Puedes consultarlo en Pacientes.");
                });

        Puma p = nuevo
                ? new Puma()
                : patients.findById(form.id()).orElseThrow(() ->
                        new BusinessException("El paciente indicado no existe."));

        if (nuevo) {
            p.setId(ids.next("PAC-", patients.findAllIds()));

            p.setCreated(clock.today());
        }

        p.setDni(dni);
        p.setFirstName(trim(form.first()));
        p.setLastName(trim(form.last()));
        p.setBirth(LocalDate.parse(trim(form.birth())));
        p.setSex(trim(form.sex()));
        p.setPhone(trim(form.phone()));
        p.setEmail(trim(form.email()));
        p.setAddress(trim(form.address()));
        p.setNotes(trim(form.notes()));

        Puma guardado = patients.save(p);

        audit.record(session.userName(), session.role(),
                nuevo ? "Paciente creado" : "Paciente actualizado",
                "Pacientes", guardado.getId());

        return guardado;
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
