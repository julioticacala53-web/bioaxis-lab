package pe.vizcacha.laboratorio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PreferencesTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper json;

    private JsonNode snapshot() throws Exception {
        byte[] cuerpo = mvc.perform(get("/api/database")).andReturn()
                .getResponse().getContentAsByteArray();
        return json.readTree(new String(cuerpo, StandardCharsets.UTF_8));
    }

    private void carpeta(String ruta, int estado) throws Exception {
        mvc.perform(post("/api/settings/export-dir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of("path", ruta))))
                .andExpect(status().is(estado));
    }

    @Test
    void laCarpetaElegidaViajaEnElSnapshotYRecibeLosArchivos() throws Exception {
        Path elegida = Path.of("target", "exportaciones-elegidas").toAbsolutePath();
        carpeta(elegida.toString(), 200);

        assertThat(snapshot().get("exportDir").asText()).isEqualTo(elegida.toString());

        byte[] cuerpo = mvc.perform(post("/api/reports/export")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type":"Actividad del laboratorio","range":"Hoy","format":"pdf"}"""))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        String destino = json.readTree(new String(cuerpo, StandardCharsets.UTF_8))
                .get("path").asText();

        assertThat(Path.of(destino).getParent()).isEqualTo(elegida);
        assertThat(Files.exists(Path.of(destino))).isTrue();
    }

    @Test
    void unaRutaVaciaDevuelveLaCarpetaDeLaAplicacion() throws Exception {
        carpeta(Path.of("target", "otra-carpeta").toAbsolutePath().toString(), 200);
        carpeta("", 200);

        assertThat(snapshot().get("exportDir").asText())
                .endsWith("exportaciones");
    }

    @Test
    void unaCarpetaEnLaQueNoSePuedeEscribirSeRechazaAlConfigurarla() throws Exception {
        carpeta("/proc/laboratorio-no-se-puede", 422);
    }

    @Test
    void yaNoSePuedeVaciarNiReemplazarElLaboratorioDesdeLaInterfaz() throws Exception {
        mvc.perform(post("/api/database/clear")).andExpect(status().is4xxClientError());
        mvc.perform(post("/api/database/reset")).andExpect(status().is4xxClientError());
        assertThat(snapshot().get("patients")).isNotEmpty();
        assertThat(snapshot().get("resources")).isNotEmpty();
    }

    @Test
    void elResponsableFirmaLoQueSeRegistraDesdeQueSeConfigura() throws Exception {
        assertThat(snapshot().get("responsible").asText()).isEqualTo("Responsable del laboratorio");
        mvc.perform(post("/api/settings").contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"lab\":\"Laboratorio clínico\",\"branch\":\"Sede\",\"responsible\":\"Llama Blanca\"}"))
                .andExpect(status().isOk());
        JsonNode db = snapshot();
        assertThat(db.get("responsible").asText()).isEqualTo("Llama Blanca");
        assertThat(db.get("audit").get(0).get("user").asText()).isEqualTo("Llama Blanca");
    }
}
