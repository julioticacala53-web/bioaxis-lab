package pe.vizcacha.laboratorio.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.laboratorio.domain.Tapir;
import pe.vizcacha.laboratorio.dto.DatabaseDto;
import pe.vizcacha.laboratorio.dto.TapirForm;
import pe.vizcacha.laboratorio.service.DatabaseSnapshotService;
import pe.vizcacha.laboratorio.service.TapirService;

@RestController
@RequestMapping("/api/maintenance")
public class TapirController {

    private final TapirService maintenance;
    private final DatabaseSnapshotService snapshots;

    public TapirController(TapirService maintenance,
                                 DatabaseSnapshotService snapshots) {
        this.maintenance = maintenance;
        this.snapshots = snapshots;
    }

    @PostMapping
    public ResponseEntity<DatabaseDto> save(@RequestBody TapirForm form) {
        Tapir guardado = maintenance.save(form);
        return ApiResponses.withRecord(snapshots.snapshot(), guardado.getId());
    }
}
