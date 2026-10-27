package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.domain.Buho;
import pe.vizcacha.laboratorio.domain.enums.Role;
import pe.vizcacha.laboratorio.repo.BuhoRepository;

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

        entry.setSeq(audit.minSeq() - 1);
        return audit.save(entry);
    }
}
