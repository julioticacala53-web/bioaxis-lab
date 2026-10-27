package pe.vizcacha.laboratorio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.service.BackupService;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BackupTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper json;
    @Autowired private BackupService respaldos;

    private List<String> lineas() throws Exception {
        byte[] cuerpo = mvc.perform(get("/api/backup/patients.csv"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        org.hamcrest.Matchers.containsString("Respaldo-pacientes-")))
                .andReturn().getResponse().getContentAsByteArray();
        String texto = new String(cuerpo, StandardCharsets.UTF_8);
        assertThat(texto).startsWith("﻿");
        return Arrays.asList(texto.substring(1).split("\r\n"));
    }

    private JsonNode snapshot() throws Exception {
        return json.readTree(mvc.perform(get("/api/database"))
                .andReturn().getResponse().getContentAsByteArray());
    }

    @Test
    void elRespaldoTraeATodosLosPacientesConSusDatos() throws Exception {
        List<String> lineas = lineas();
        assertThat(lineas.get(0)).startsWith("\"Código del paciente\",\"DNI\",\"Nombres\"");
        for (JsonNode p : snapshot().get("patients")) {
            assertThat(lineas).anyMatch(l -> l.startsWith("\"" + p.get("id").asText() + "\",\""
                    + p.get("dni").asText() + "\""));
        }
    }

    @Test
    void losResultadosPublicadosVanConValorYLosDemasNo() throws Exception {
        List<String> lineas = lineas();
        JsonNode db = snapshot();
        JsonNode publicada = null;
        JsonNode pendiente = null;
        for (JsonNode s : db.get("samples")) {
            if (publicada == null && s.get("state").asText().equals("PUBLICADA")) publicada = s;
            if (pendiente == null && s.get("state").asText().equals("EN VALIDACIÓN")) pendiente = s;
        }
        String idPublicada = "\"" + publicada.get("id").asText() + "\"";
        assertThat(lineas.stream().filter(l -> l.contains(idPublicada)).count()).isGreaterThan(1);
        assertThat(lineas).anyMatch(l -> l.contains(idPublicada) && l.contains("\"PUBLICADA\""));

        String idPendiente = "\"" + pendiente.get("id").asText() + "\"";
        List<String> suyas = lineas.stream().filter(l -> l.contains(idPendiente)).toList();
        assertThat(suyas).hasSize(1);
        assertThat(suyas.get(0)).contains("\"Resultado no publicado\"");
        for (String valor : pendiente.get("results").properties().stream()
                .map(e -> e.getValue().asText()).filter(v -> v.length() > 2).toList()) {
            assertThat(suyas.get(0)).doesNotContain("\"" + valor + "\"");
        }
    }

    @Test
    void exportarElRespaldoQuedaEnAuditoria() throws Exception {
        lineas();
        assertThat(snapshot().get("audit").get(0).get("action").asText())
                .isEqualTo("Respaldo de pacientes exportado");
    }

    @Test
    void seGuardaEnLaRutaElegida() throws Exception {
        Path destino = Files.createTempDirectory("respaldo").resolve("copia.csv");
        respaldos.saveTo(destino);
        assertThat(Files.readString(destino, StandardCharsets.UTF_8)).contains("Código del paciente");
    }

    @Test
    void unCampoQueEmpiezaComoFormulaNoSeEjecutaEnExcel() {
        assertThat(campo("=HYPERLINK(\"x\")")).isEqualTo("\"'=HYPERLINK(\"\"x\"\")\"");
        assertThat(campo("-3.5")).isEqualTo("\"-3.5\"");
        assertThat(campo("Alpaca, \"la de\" Altiplano")).isEqualTo("\"Alpaca, \"\"la de\"\" Altiplano\"");
    }

    private static String campo(String v) {
        try {
            var m = BackupService.class.getDeclaredMethod("campo", String.class);
            m.setAccessible(true);
            return (String) m.invoke(null, v);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
