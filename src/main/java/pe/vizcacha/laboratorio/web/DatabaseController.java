package pe.vizcacha.laboratorio.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.laboratorio.dto.DatabaseDto;
import pe.vizcacha.laboratorio.service.DatabaseSnapshotService;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    private final DatabaseSnapshotService snapshots;

    public DatabaseController(DatabaseSnapshotService snapshots) {
        this.snapshots = snapshots;
    }

    @GetMapping
    public DatabaseDto database() {
        return snapshots.snapshot();
    }
}
