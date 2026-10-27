package pe.vizcacha.laboratorio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExportTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper json;
    @Autowired private pe.vizcacha.laboratorio.service.ExportService exportaciones;
    @Autowired private pe.vizcacha.laboratorio.web.ReportController reportes;

    private JsonNode exportar(String tipo, String rango, String formato) throws Exception {
        byte[] cuerpo = mvc.perform(post("/api/reports/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of(
                                "type", tipo, "range", rango, "format", formato))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        return json.readTree(new String(cuerpo, StandardCharsets.UTF_8));
    }

    @Test
    void generaUnPdfDeVerdad() throws Exception {
        JsonNode r = exportar("Actividad del laboratorio", "Este mes", "pdf");

        Path archivo = Path.of(r.get("path").asText());
        assertThat(archivo).exists();
        assertThat(r.get("fileName").asText())
                .startsWith("Reporte-actividad-del-laboratorio-")
                .endsWith(".pdf");

        byte[] bytes = Files.readAllBytes(archivo);

        assertThat(new String(bytes, 0, 5, StandardCharsets.ISO_8859_1)).isEqualTo("%PDF-");
        assertThat(r.get("bytes").asLong()).isGreaterThan(1000L);
    }

    @Test
    void elNombreDelArchivoNoLlevaTildesNiEspacios() throws Exception {
        JsonNode r = exportar("Exámenes más solicitados", "Hoy", "pdf");

        assertThat(r.get("fileName").asText())
                .startsWith("Reporte-examenes-mas-solicitados-")
                .matches("[A-Za-z0-9.\\-]+");
    }

    @Test
    void generaUnExcelLegibleConLasFilasDelReporte() throws Exception {
        JsonNode r = exportar("Inventario", "Hoy", "xlsx");

        Path archivo = Path.of(r.get("path").asText());
        assertThat(archivo).exists();
        assertThat(r.get("fileName").asText()).endsWith(".xlsx");

        try (InputStream in = Files.newInputStream(archivo);
             XSSFWorkbook libro = new XSSFWorkbook(in)) {

            Sheet hoja = libro.getSheetAt(0);
            assertThat(hoja.getSheetName()).isEqualTo("Inventario");

            assertThat(hoja.getRow(0).getCell(0).getStringCellValue())
                    .contains("Laboratorio clínico");
            assertThat(hoja.getRow(1).getCell(0).getStringCellValue())
                    .contains("Inventario")
                    .contains("Período: Hoy");

            Row encabezados = hoja.getRow(3);
            assertThat(encabezados.getCell(0).getStringCellValue()).isEqualTo("Recurso");
            assertThat(encabezados.getCell(1).getStringCellValue()).isEqualTo("Tipo");
            assertThat(encabezados.getCell(2).getStringCellValue()).isEqualTo("Stock actual");
            assertThat(encabezados.getCell(3).getStringCellValue()).isEqualTo("Estado");

            Row primera = hoja.getRow(4);
            assertThat(primera.getCell(0).getStringCellValue())
                    .isEqualTo("Reactivo hematológico");
            assertThat(primera.getCell(2).getStringCellValue()).isEqualTo("85 pruebas");

            assertThat(hoja.getRow(16).getCell(0).getStringCellValue())
                    .isEqualTo("Puntas de micropipeta");
        }
    }

    @Test
    void exportaUnReporteVacioSinRomperse() throws Exception {

        JsonNode r = exportar("Consumo de reactivos", "Hoy", "pdf");

        assertThat(Path.of(r.get("path").asText())).exists();
        assertThat(r.get("bytes").asLong()).isGreaterThan(1000L);
    }

    @Test
    void rechazaUnFormatoNoSoportado() throws Exception {
        mvc.perform(post("/api/reports/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of(
                                "type", "Inventario", "range", "Hoy", "format", "docx"))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error")
                        .value("Formato de exportación no soportado: docx"));
    }

    @Test
    void elReporteSeGuardaEnLaRutaQueSeElige() throws Exception {
        Path carpeta = Files.createTempDirectory("ruta-elegida");
        Path destino = carpeta.resolve("sub").resolve("mi-reporte.xlsx");
        var reporte = reportes.reporte(new pe.vizcacha.laboratorio.dto.ExportForm(
                "Inventario", "Hoy", null, null, "xlsx"));

        var r = exportaciones.exportTo(reporte, "xlsx", "Laboratorio clínico", "Sede", destino);

        assertThat(destino).exists();
        assertThat(r.path()).isEqualTo(destino.toAbsolutePath().toString());
        assertThat(exportaciones.reportFileName(reporte, "pdf")).startsWith("Reporte-").endsWith(".pdf");
    }

    @Test
    void elSelectorDeArchivoSoloExisteEnLaVentana() throws Exception {
        mvc.perform(post("/api/desktop/save-report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of(
                                "type", "Inventario", "range", "Hoy", "format", "pdf"))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value("El selector de carpetas solo está "
                        + "disponible en la aplicación de escritorio."));
    }
}
