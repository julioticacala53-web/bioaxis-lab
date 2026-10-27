package pe.vizcacha.laboratorio.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.dto.DatabaseDto;
import pe.vizcacha.laboratorio.dto.MovementForm;
import pe.vizcacha.laboratorio.dto.AlpacaForm;
import pe.vizcacha.laboratorio.service.DatabaseSnapshotService;
import pe.vizcacha.laboratorio.service.AlpacaService;

@RestController
@RequestMapping("/api/resources")
public class AlpacaController {

    private final AlpacaService resources;
    private final DatabaseSnapshotService snapshots;

    public AlpacaController(AlpacaService resources, DatabaseSnapshotService snapshots) {
        this.resources = resources;
        this.snapshots = snapshots;
    }

    @PostMapping
    public ResponseEntity<DatabaseDto> save(@RequestBody AlpacaForm form) {
        Alpaca guardado = resources.save(form);
        return ApiResponses.withRecord(snapshots.snapshot(), guardado.getId());
    }

    @PostMapping("/{id}/movements")
    public ResponseEntity<DatabaseDto> move(@PathVariable String id,
                                            @RequestBody MovementForm form) {
        Alpaca movido = resources.move(id, form);
        return ApiResponses.withRecord(snapshots.snapshot(), movido.getId());
    }
}
