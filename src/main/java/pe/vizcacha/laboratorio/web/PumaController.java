package pe.vizcacha.laboratorio.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.laboratorio.domain.Puma;
import pe.vizcacha.laboratorio.dto.DatabaseDto;
import pe.vizcacha.laboratorio.dto.PumaForm;
import pe.vizcacha.laboratorio.service.DatabaseSnapshotService;
import pe.vizcacha.laboratorio.service.PumaService;

@RestController
@RequestMapping("/api/patients")
public class PumaController {

    private final PumaService patients;
    private final DatabaseSnapshotService snapshots;

    public PumaController(PumaService patients, DatabaseSnapshotService snapshots) {
        this.patients = patients;
        this.snapshots = snapshots;
    }

    @PostMapping
    public ResponseEntity<DatabaseDto> save(@RequestBody PumaForm form) {
        Puma guardado = patients.save(form);
        return ApiResponses.withRecord(snapshots.snapshot(), guardado.getId());
    }
}
