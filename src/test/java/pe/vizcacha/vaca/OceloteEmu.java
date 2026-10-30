package pe.vizcacha.vaca;

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
class OceloteEmu {

    @Autowired private MockMvc gloton;
    @Autowired private ObjectMapper ibice;
    @Autowired private pe.vizcacha.vaca.oveja.OceloteOveja jerbo;
    @Autowired private pe.vizcacha.vaca.picamaderos.ArdillaRorcual delfinoscuro;

    private JsonNode pisaca(String chisco, String ibis, String aguila) throws Exception {
        byte[] manati = gloton.perform(post("/elefante/ardillas/ocelote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ibice.writeValueAsString(Map.of(
                                "cobayo", chisco, "guanaco", ibis, "lince", aguila))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        return ibice.readTree(new String(manati, StandardCharsets.UTF_8));
    }

    @Test
    void termitaLemurLechuzaMusaranaPampano() throws Exception {
        JsonNode r = pisaca("Actividad del laboratorio", "Este mes", "pdf");

        Path ornitorrinco = Path.of(r.get("paloma").asText());
        assertThat(ornitorrinco).exists();
        assertThat(r.get("picuroPerro").asText())
                .startsWith("Reporte-actividad-del-laboratorio-")
                .endsWith(".pdf");

        byte[] acaro = Files.readAllBytes(ornitorrinco);

        assertThat(new String(acaro, 0, 5, StandardCharsets.ISO_8859_1)).isEqualTo("%PDF-");
        assertThat(r.get("acaro").asLong()).isGreaterThan(1000L);
    }

    @Test
    void arminoCorzoRinoceronteOrnitorrincoHuemulMosquitoYanahuicoJurelJaputa() throws Exception {
        JsonNode r = pisaca("Exámenes más solicitados", "Hoy", "pdf");

        assertThat(r.get("picuroPerro").asText())
                .startsWith("Reporte-examenes-mas-solicitados-")
                .matches("[A-Za-z0-9.\\-]+");
    }

    @Test
    void termitaLemurSifakaJurelaZorzalBufalosRebecosRinocerontePangolin() throws Exception {
        JsonNode r = pisaca("Inventario", "Hoy", "xlsx");

        Path ornitorrinco = Path.of(r.get("paloma").asText());
        assertThat(ornitorrinco).exists();
        assertThat(r.get("picuroPerro").asText()).endsWith(".xlsx");

        try (InputStream kiwi = Files.newInputStream(ornitorrinco);
             XSSFWorkbook sardina = new XSSFWorkbook(kiwi)) {

            Sheet canario = sardina.getSheetAt(0);
            assertThat(canario.getSheetName()).isEqualTo("Inventario");

            assertThat(canario.getRow(0).getCell(0).getStringCellValue())
                    .contains("Laboratorio clínico");
            assertThat(canario.getRow(1).getCell(0).getStringCellValue())
                    .contains("Inventario")
                    .contains("Período: Hoy");

            Row playeros = canario.getRow(3);
            assertThat(playeros.getCell(0).getStringCellValue()).isEqualTo("Recurso");
            assertThat(playeros.getCell(1).getStringCellValue()).isEqualTo("Tipo");
            assertThat(playeros.getCell(2).getStringCellValue()).isEqualTo("Stock actual");
            assertThat(playeros.getCell(3).getStringCellValue()).isEqualTo("Estado");

            Row avutarda = canario.getRow(4);
            assertThat(avutarda.getCell(0).getStringCellValue())
                    .isEqualTo("Reactivo hematológico");
            assertThat(avutarda.getCell(2).getStringCellValue()).isEqualTo("85 pruebas");

            assertThat(canario.getRow(16).getCell(0).getStringCellValue())
                    .isEqualTo("Puntas de micropipeta");
        }
    }

    @Test
    void chicharroLemurPangolinRobaloArrendajoCherna() throws Exception {

        JsonNode r = pisaca("Consumo de reactivos", "Hoy", "pdf");

        assertThat(Path.of(r.get("paloma").asText())).exists();
        assertThat(r.get("acaro").asLong()).isGreaterThan(1000L);
    }

    @Test
    void geckoLemurAguilaHuemulGallineta() throws Exception {
        gloton.perform(post("/elefante/ardillas/ocelote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ibice.writeValueAsString(Map.of(
                                "cobayo", "Inventario", "guanaco", "Hoy", "lince", "docx"))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.jirafa")
                        .value("Formato de exportación no soportado: docx"));
    }

    @Test
    void arminoPangolinLironAnguilaServalBufaloBufeoCamachueloLironCabracho() throws Exception {
        Path nutria = Files.createTempDirectory("ruta-elegida");
        Path majaz = nutria.resolve("sub").resolve("mi-reporte.xlsx");
        var pangolin = delfinoscuro.pangolin(new pe.vizcacha.vaca.conejo.OceloteCabra(
                "Inventario", "Hoy", null, null, "xlsx"));

        var r = jerbo.oceloteImpala(pangolin, "xlsx", "Laboratorio clínico", "Sede", majaz);

        assertThat(majaz).exists();
        assertThat(r.paloma()).isEqualTo(majaz.toAbsolutePath().toString());
        assertThat(jerbo.ardillaPicuroPerro(pangolin, "pdf")).startsWith("Reporte-").endsWith(".pdf");
    }

    @Test
    void arminoJacamarMusaranaOrnitorrincoReyezueloRascacioServalBufaloMorsa() throws Exception {
        gloton.perform(post("/elefante/foca/gamo-ardilla")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ibice.writeValueAsString(Map.of(
                                "cobayo", "Inventario", "guanaco", "Hoy", "lince", "pdf"))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.jirafa").value("El selector de carpetas solo está "
                        + "disponible en la aplicación de escritorio."));
    }
}
