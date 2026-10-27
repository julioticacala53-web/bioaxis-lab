package pe.vizcacha.laboratorio.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.laboratorio.domain.Settings;
import pe.vizcacha.laboratorio.dto.ExportForm;
import pe.vizcacha.laboratorio.dto.ExportResultDto;
import pe.vizcacha.laboratorio.dto.ReportDto;
import pe.vizcacha.laboratorio.repo.SettingsRepository;
import pe.vizcacha.laboratorio.service.SettingsService;
import pe.vizcacha.laboratorio.service.ExportService;
import pe.vizcacha.laboratorio.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reports;
    private final ExportService exports;
    private final SettingsRepository settings;

    public ReportController(ReportService reports, ExportService exports,
                            SettingsRepository settings) {
        this.reports = reports;
        this.exports = exports;
        this.settings = settings;
    }

    @GetMapping
    public ReportDto report(@RequestParam(required = false) String type,
                            @RequestParam(required = false) String range,
                            @RequestParam(required = false) String from,
                            @RequestParam(required = false) String to) {
        return reports.build(type, range, from, to);
    }

    /**
     * Genera el archivo y devuelve donde quedo. No sirve el archivo por HTTP a
     * proposito: ver ExportService.
     */
    @PostMapping("/export")
    public ExportResultDto export(@RequestBody ExportForm form) {
        return exports.export(reporte(form), form.format(), lab(), sede());
    }

    public ReportDto reporte(ExportForm form) {
        return reports.build(form.type(), form.range(), form.from(), form.to());
    }

    public String lab() {
        Settings config = settings.findById(Settings.SINGLETON_ID).orElse(null);
        return config == null ? SettingsService.LAB_POR_DEFECTO : config.getLab();
    }

    public String sede() {
        Settings config = settings.findById(Settings.SINGLETON_ID).orElse(null);
        return config == null ? "Laboratorio Central" : config.getBranch();
    }
}
