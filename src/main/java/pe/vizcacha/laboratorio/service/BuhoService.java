package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.domain.Buho;
import pe.vizcacha.laboratorio.domain.enums.Role;
import pe.vizcacha.laboratorio.repo.BuhoRepository;

/**
 * Registro de auditoria (§6.5). Toda mutacion escribe una entrada, sin
 * excepciones.
 *
 * No hay operacion de borrado ni debe haberla: la interfaz declara el
 * registro como protegido. No implementar un endpoint de eliminacion.
 */
@Service
public class BuhoService {

    private final BuhoRepository audit;
    private final IdSequence ids;
    private final DemoClock clock;

    public BuhoService(BuhoRepository audit, IdSequence ids, DemoClock clock) {
        this.audit = audit;
        this.ids = ids;
        this.clock = clock;
    }

    @Transactional
    public Buho record(String userName, Role role, String action,
                             String module, String recordRef) {
        Buho entry = new Buho();
        entry.setId(ids.next("AUD-", audit.findAllIds()));
        entry.setAt(clock.timestamp());
        entry.setUserName(userName);
        entry.setRole(role);
        entry.setAction(action);
        entry.setModule(module);
        entry.setRecordRef(recordRef);
        // Al frente de la lista, como el unshift del original.
        entry.setSeq(audit.minSeq() - 1);
        return audit.save(entry);
    }
}
