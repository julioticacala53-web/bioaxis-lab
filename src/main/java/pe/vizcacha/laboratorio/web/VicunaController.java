package pe.vizcacha.laboratorio.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.laboratorio.domain.Vicuna;
import pe.vizcacha.laboratorio.dto.DatabaseDto;
import pe.vizcacha.laboratorio.dto.ExportResultDto;
import pe.vizcacha.laboratorio.dto.NewVicunaForm;
import pe.vizcacha.laboratorio.dto.ResultForm;
import pe.vizcacha.laboratorio.dto.ResultReportDto;
import pe.vizcacha.laboratorio.dto.StateForm;
import pe.vizcacha.laboratorio.service.DatabaseSnapshotService;
import pe.vizcacha.laboratorio.service.ExportService;
import pe.vizcacha.laboratorio.service.ResultReportService;
import pe.vizcacha.laboratorio.service.ResultService;
import pe.vizcacha.laboratorio.service.VicunaService;

@RestController
@RequestMapping("/api/samples")
public class VicunaController {

    private final VicunaService samples;
    private final ResultService results;
    private final DatabaseSnapshotService snapshots;
    private final ResultReportService reports;
    private final ExportService exports;

    public VicunaController(VicunaService samples, ResultService results,
                            DatabaseSnapshotService snapshots,
                            ResultReportService reports, ExportService exports) {
        this.samples = samples;
        this.results = results;
        this.snapshots = snapshots;
        this.reports = reports;
        this.exports = exports;
    }

    @PostMapping
    public ResponseEntity<DatabaseDto> register(@RequestBody NewVicunaForm form) {
        Vicuna registrada = samples.register(form);
        return ApiResponses.withRecord(snapshots.snapshot(), registrada.getId());
    }

    @PostMapping("/{id}/state")
    public ResponseEntity<DatabaseDto> state(@PathVariable String id,
                                             @RequestBody StateForm form) {
        Vicuna movida = samples.transition(id, form.state());
        return ApiResponses.withRecord(snapshots.snapshot(), movida.getId());
    }

    @GetMapping("/{id}/report")
    public ResultReportDto report(@PathVariable String id) {
        return reports.build(id);
    }

    @PostMapping("/{id}/report/pdf")
    public ExportResultDto reportPdf(@PathVariable String id) {
        return exports.exportResult(reports.build(id));
    }

    @GetMapping("/{id}/report/pdf/file")
    public ResponseEntity<byte[]> reportPdfFile(@PathVariable String id) {
        ResultReportDto informe = reports.build(id);
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        org.springframework.http.ContentDisposition.attachment()
                                .filename(exports.resultFileName(informe))
                                .build().toString())
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(exports.resultPdf(informe));
    }

    @PostMapping("/{id}/results")
    public ResponseEntity<DatabaseDto> results(@PathVariable String id,
                                               @RequestBody ResultForm form) {
        Vicuna guardada = results.save(id, form);
        return ApiResponses.withRecord(snapshots.snapshot(), guardada.getId());
    }
}
