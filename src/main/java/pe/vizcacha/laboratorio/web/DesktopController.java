package pe.vizcacha.laboratorio.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.laboratorio.VentanaVizcacha;
import pe.vizcacha.laboratorio.domain.Preference;
import pe.vizcacha.laboratorio.dto.ExportForm;
import pe.vizcacha.laboratorio.dto.ExportResultDto;
import pe.vizcacha.laboratorio.dto.ReportDto;
import pe.vizcacha.laboratorio.dto.ResultReportDto;
import pe.vizcacha.laboratorio.service.BusinessException;
import pe.vizcacha.laboratorio.service.ExportService;
import pe.vizcacha.laboratorio.service.PreferenceService;
import pe.vizcacha.laboratorio.service.ResultReportService;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/desktop")
public class DesktopController {

    private final ResultReportService informes;
    private final ExportService exportaciones;
    private final PreferenceService preferencias;
    private final ReportController reportes;
    private final pe.vizcacha.laboratorio.service.BackupService respaldos;
    private final pe.vizcacha.laboratorio.service.PurgeService depuracion;

    public DesktopController(ResultReportService informes, ExportService exportaciones,
                             PreferenceService preferencias, ReportController reportes,
                             pe.vizcacha.laboratorio.service.BackupService respaldos,
                             pe.vizcacha.laboratorio.service.PurgeService depuracion) {
        this.depuracion = depuracion;
        this.respaldos = respaldos;
        this.informes = informes;
        this.exportaciones = exportaciones;
        this.preferencias = preferencias;
        this.reportes = reportes;
    }

    @PostMapping("/save-report")
    public Map<String, Object> guardarReporte(@RequestBody ExportForm form) {
        soloEnLaVentana();
        String formato = exportaciones.formato(form.format());
        ReportDto reporte = reportes.reporte(form);
        boolean pdf = ExportService.PDF.equals(formato);
        String ruta = pedirArchivo(
                pdf ? "Guardar el reporte en PDF" : "Guardar el reporte en Excel",
                exportaciones.reportFileName(reporte, formato),
                pdf ? "Documento PDF" : "Libro de Excel", "." + formato,
                carpetaExistente(exportaciones.carpeta().toAbsolutePath().toString()));
        if (ruta.isEmpty()) {
            return Map.of("path", "", "cancelled", true);
        }
        java.nio.file.Path destino = java.nio.file.Paths.get(ruta);
        ExportResultDto guardado = exportaciones.exportTo(reporte, formato,
                reportes.lab(), reportes.sede(), destino);
        preferencias.set(Preference.CARPETA_EXPORTACIONES,
                destino.toAbsolutePath().getParent().toString());
        return Map.of("path", guardado.path(), "fileName", guardado.fileName(),
                "cancelled", false);
    }

    @PostMapping("/save-backup")
    public Map<String, Object> guardarRespaldo() {
        soloEnLaVentana();
        String ruta = pedirArchivo("Guardar el respaldo de pacientes", respaldos.fileName(),
                "Archivo CSV", ".csv",
                carpetaExistente(exportaciones.carpeta().toAbsolutePath().toString()));
        if (ruta.isEmpty()) {
            return Map.of("path", "", "cancelled", true);
        }
        ExportResultDto guardado = respaldos.saveTo(java.nio.file.Paths.get(ruta));
        return Map.of("path", guardado.path(), "fileName", guardado.fileName(),
                "cancelled", false);
    }

    @PostMapping("/purge")
    public Map<String, Object> respaldarYEliminar(@RequestBody Map<String, String> cuerpo) {
        soloEnLaVentana();
        java.time.LocalDate fecha = depuracion.limite(cuerpo.get("before"));
        depuracion.exigirPlan(fecha);
        String ruta = pedirArchivo("Guardar el respaldo antes de eliminar", depuracion.fileName(fecha),
                "Archivo CSV", ".csv",
                carpetaExistente(exportaciones.carpeta().toAbsolutePath().toString()));
        if (ruta.isEmpty()) {
            return Map.of("path", "", "cancelled", true);
        }
        ExportResultDto guardado = depuracion.respaldarYEliminar(fecha, java.nio.file.Paths.get(ruta));
        return Map.of("path", guardado.path(), "fileName", guardado.fileName(),
                "cancelled", false);
    }

    private void soloEnLaVentana() {
        if (!Boolean.getBoolean("laboratorio.ventana")) {
            throw new BusinessException("El selector de carpetas solo está "
                    + "disponible en la aplicación de escritorio.");
        }
    }

    private String pedirArchivo(String titulo, String nombre, String tipo, String extension,
                                File inicial) {
        CompletableFuture<String> elegido = new CompletableFuture<>();
        javafx.application.Platform.runLater(() -> {
            try {
                javafx.stage.FileChooser selector = new javafx.stage.FileChooser();
                selector.setTitle(titulo);
                selector.setInitialFileName(nombre);
                selector.getExtensionFilters().add(
                        new javafx.stage.FileChooser.ExtensionFilter(tipo, "*" + extension));
                if (inicial != null) {
                    selector.setInitialDirectory(inicial);
                }
                File archivo = selector.showSaveDialog(VentanaVizcacha.principal());
                elegido.complete(archivo == null ? "" : archivo.getAbsolutePath());
            } catch (RuntimeException e) {
                elegido.completeExceptionally(e);
            }
        });
        String ruta;
        try {
            ruta = elegido.get(5, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new BusinessException("No se pudo abrir la ventana para elegir la carpeta.");
        }
        if (!ruta.isEmpty() && !ruta.toLowerCase(java.util.Locale.ROOT).endsWith(extension)) {
            ruta = ruta + extension;
        }
        return ruta;
    }

    private File carpetaExistente(String... candidatas) {
        for (String candidata : candidatas) {
            if (candidata == null || candidata.isBlank()) {
                continue;
            }
            File carpeta = new File(candidata);
            if (carpeta.isDirectory()) {
                return carpeta;
            }
        }
        File casa = new File(System.getProperty("user.home"));
        return casa.isDirectory() ? casa : null;
    }

    @PostMapping("/save-result/{sampleId}")
    public Map<String, Object> guardarResultado(@PathVariable String sampleId) {
        soloEnLaVentana();
        ResultReportDto informe = informes.build(sampleId);
        String ruta = pedirArchivo("Guardar el informe de resultados",
                exportaciones.resultFileName(informe), "Documento PDF", ".pdf",
                carpetaExistente(preferencias.get(Preference.CARPETA_RESULTADOS, ""),
                        exportaciones.carpeta().toAbsolutePath().toString()));
        if (ruta.isEmpty()) {
            return Map.of("path", "", "cancelled", true);
        }
        java.nio.file.Path destino = java.nio.file.Paths.get(ruta);
        ExportResultDto guardado = exportaciones.exportResultTo(informe, destino);
        preferencias.set(Preference.CARPETA_RESULTADOS,
                destino.toAbsolutePath().getParent().toString());
        return Map.of("path", guardado.path(), "fileName", guardado.fileName(),
                "cancelled", false);
    }

    @PostMapping("/print/{sampleId}")
    public ExportResultDto imprimir(@PathVariable String sampleId) {
        ExportResultDto archivo = exportaciones.exportResult(informes.build(sampleId));
        abrir(new File(archivo.path()));
        return archivo;
    }

    @PostMapping("/folder")
    public Map<String, String> elegirCarpeta() {
        if (!Boolean.getBoolean("laboratorio.ventana")) {
            throw new BusinessException("El selector de carpetas solo está "
                    + "disponible en la aplicación de escritorio. Escribe la ruta.");
        }
        CompletableFuture<String> elegida = new CompletableFuture<>();
        javafx.application.Platform.runLater(() -> {
            try {
                javafx.stage.DirectoryChooser selector = new javafx.stage.DirectoryChooser();
                selector.setTitle("Carpeta para los PDF y los Excel");
                File inicial = carpetaExistente(exportaciones.carpeta().toAbsolutePath().toString());
                if (inicial != null) {
                    selector.setInitialDirectory(inicial);
                }
                File carpeta = selector.showDialog(VentanaVizcacha.principal());
                elegida.complete(carpeta == null ? "" : carpeta.getAbsolutePath());
            } catch (RuntimeException e) {
                elegida.completeExceptionally(e);
            }
        });
        try {
            return Map.of("path", elegida.get(3, TimeUnit.MINUTES));
        } catch (Exception e) {
            throw new BusinessException("No se pudo abrir el selector de carpetas. "
                    + "Escribe la ruta a mano.");
        }
    }

    private void abrir(File archivo) {
        if (!Desktop.isDesktopSupported()
                || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            throw new BusinessException("El sistema no permite abrir el archivo. "
                    + "El informe quedó guardado en " + archivo.getAbsolutePath());
        }
        try {
            Desktop.getDesktop().open(archivo);
        } catch (IOException e) {
            throw new BusinessException("No hay un visor de PDF configurado. "
                    + "El informe quedó guardado en " + archivo.getAbsolutePath());
        }
    }
}
