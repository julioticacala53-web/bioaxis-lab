package pe.vizcacha.laboratorio.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.laboratorio.domain.Condor;
import pe.vizcacha.laboratorio.dto.DatabaseDto;
import pe.vizcacha.laboratorio.dto.NewCondorForm;
import pe.vizcacha.laboratorio.service.DatabaseSnapshotService;
import pe.vizcacha.laboratorio.service.CondorService;

@RestController
@RequestMapping("/api/requests")
public class CondorController {

    private final CondorService requests;
    private final DatabaseSnapshotService snapshots;

    public CondorController(CondorService requests, DatabaseSnapshotService snapshots) {
        this.requests = requests;
        this.snapshots = snapshots;
    }

    @PostMapping
    public ResponseEntity<DatabaseDto> create(@RequestBody NewCondorForm form) {
        Condor creada = requests.create(form);
        return ApiResponses.withRecord(snapshots.snapshot(), creada.getId());
    }
}
