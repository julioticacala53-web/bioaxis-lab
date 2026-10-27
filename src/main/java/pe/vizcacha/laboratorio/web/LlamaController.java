package pe.vizcacha.laboratorio.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.laboratorio.domain.Llama;
import pe.vizcacha.laboratorio.dto.DatabaseDto;
import pe.vizcacha.laboratorio.dto.LlamaForm;
import pe.vizcacha.laboratorio.service.DatabaseSnapshotService;
import pe.vizcacha.laboratorio.service.LlamaService;

@RestController
@RequestMapping("/api/exams")
public class LlamaController {

    private final LlamaService exams;
    private final DatabaseSnapshotService snapshots;

    public LlamaController(LlamaService exams, DatabaseSnapshotService snapshots) {
        this.exams = exams;
        this.snapshots = snapshots;
    }

    @PostMapping
    public ResponseEntity<DatabaseDto> save(@RequestBody LlamaForm form) {
        Llama guardado = exams.save(form);
        return ApiResponses.withRecord(snapshots.snapshot(), guardado.getId());
    }
}
