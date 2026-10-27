package pe.vizcacha.laboratorio.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.laboratorio.dto.AppearanceForm;
import pe.vizcacha.laboratorio.dto.DatabaseDto;
import pe.vizcacha.laboratorio.dto.ExportDirForm;
import pe.vizcacha.laboratorio.dto.NotificationsForm;
import pe.vizcacha.laboratorio.dto.SettingsForm;
import pe.vizcacha.laboratorio.service.DatabaseSnapshotService;
import pe.vizcacha.laboratorio.service.SettingsService;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final SettingsService settings;
    private final DatabaseSnapshotService snapshots;

    public SettingsController(SettingsService settings, DatabaseSnapshotService snapshots) {
        this.settings = settings;
        this.snapshots = snapshots;
    }

    @PostMapping
    public ResponseEntity<DatabaseDto> general(@RequestBody SettingsForm form) {
        settings.saveGeneral(form);
        return ApiResponses.withRecord(snapshots.snapshot(), "General");
    }

    @PostMapping("/export-dir")
    public ResponseEntity<DatabaseDto> exportDir(@RequestBody ExportDirForm form) {
        settings.setExportDir(form.path());
        return ApiResponses.withRecord(snapshots.snapshot(), "Exportaciones");
    }

    @PostMapping("/appearance")
    public ResponseEntity<DatabaseDto> appearance(@RequestBody AppearanceForm form) {
        settings.setAppearance(form.theme());
        return ApiResponses.withRecord(snapshots.snapshot(), "Apariencia");
    }

    @PostMapping("/notifications")
    public ResponseEntity<DatabaseDto> notifications(@RequestBody NotificationsForm form) {
        settings.setNotifications(form.enabled() != null && form.enabled());
        return ApiResponses.withRecord(snapshots.snapshot(), "Alertas");
    }
}
