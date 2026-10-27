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
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.service.PurgeService;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PurgeTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper json;
    @Autowired private PurgeService depuracion;
    @Autowired private DemoClock clock;

    private JsonNode get(String url) throws Exception {
        return json.readTree(mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray());
    }

    private Set<String> ids(JsonNode lista) {
        Set<String> ids = new HashSet<>();
        lista.forEach(n -> ids.add(n.get("id").asText()));
        return ids;
    }

    @Test
    void respaldaPrimeroYEliminaSoloLoEntregado() throws Exception {
        JsonNode antes = get("/api/database");
        PurgeService.Plan plan = depuracion.plan(clock.today());
        assertThat(plan.solicitudes()).isNotEmpty();
        for (JsonNode s : antes.get("samples")) {
            if (plan.muestras().contains(s.get("id").asText())) {
                assertThat(s.get("state").asText()).isEqualTo("PUBLICADA");
            }
        }

        Path destino = Files.createTempDirectory("depuracion").resolve("respaldo.csv");
        depuracion.respaldarYEliminar(clock.today(), destino);

        String csv = Files.readString(destino, StandardCharsets.UTF_8);
        plan.muestras().forEach(id -> assertThat(csv).contains("\"" + id + "\""));
        for (JsonNode p : antes.get("patients")) {
            if (plan.pacientes().contains(p.get("id").asText())) {
                assertThat(csv).contains("\"" + p.get("dni").asText() + "\"");
            }
        }

        JsonNode despues = get("/api/database");
        Set<String> muestrasDespues = ids(despues.get("samples"));
        Set<String> solicitudesDespues = ids(despues.get("requests"));
        plan.muestras().forEach(id -> assertThat(muestrasDespues).doesNotContain(id));
        plan.solicitudes().forEach(id -> assertThat(solicitudesDespues).doesNotContain(id));
        Set<String> pacientesDespues = ids(despues.get("patients"));
        plan.pacientes().forEach(id -> assertThat(pacientesDespues).doesNotContain(id));
        assertThat(despues.get("samples").size())
                .isEqualTo(antes.get("samples").size() - plan.muestras().size());
        assertThat(despues.get("resources").size()).isEqualTo(antes.get("resources").size());
        assertThat(despues.get("exams").size()).isEqualTo(antes.get("exams").size());
        assertThat(despues.get("audit").size()).isEqualTo(antes.get("audit").size() + 1);
        assertThat(despues.get("audit").get(0).get("action").asText())
                .startsWith("Respaldo y eliminación de entregados");
    }

    @Test
    void loQueEstaEnCursoYSusPacientesSeQuedan() throws Exception {
        JsonNode antes = get("/api/database");
        Set<String> conAlgoEnCurso = new HashSet<>();
        Set<String> enCurso = new HashSet<>();
        for (JsonNode s : antes.get("samples")) {
            if (!s.get("state").asText().equals("PUBLICADA")) {
                enCurso.add(s.get("id").asText());
                conAlgoEnCurso.add(s.get("patient").asText());
            }
        }
        assertThat(enCurso).isNotEmpty();
        depuracion.respaldarYEliminar(clock.today(), Files.createTempDirectory("b").resolve("b.csv"));
        JsonNode despues = get("/api/database");
        assertThat(ids(despues.get("samples"))).containsAll(enCurso);
        assertThat(ids(despues.get("patients"))).containsAll(conAlgoEnCurso);
    }

    @Test
    void siNoSePuedeGuardarElRespaldoNoSeEliminaNada() throws Exception {
        int antes = get("/api/database").get("samples").size();
        assertThatThrownBy(() -> depuracion.respaldarYEliminar(clock.today(),
                Path.of("/proc/no-se-puede/respaldo.csv")))
                .hasMessageStartingWith("No se pudo guardar el respaldo, así que no se eliminó nada");
        assertThat(get("/api/database").get("samples").size()).isEqualTo(antes);
    }

    @Test
    void repetirAvisaQueNoQuedaNada() throws Exception {
        depuracion.respaldarYEliminar(clock.today(), Files.createTempDirectory("c").resolve("c.csv"));
        mvc.perform(post("/api/purge").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"before\":\"" + clock.today() + "\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value(org.hamcrest.Matchers.startsWith(
                        "No hay solicitudes entregadas por completo antes del")));
    }

    @Test
    void noSeEliminaHaciaElFuturoNiSinVentanaElSelector() throws Exception {
        mvc.perform(post("/api/purge").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"before\":\"" + clock.today().plusDays(1) + "\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value("Solo se elimina lo entregado hasta hoy."));
        mvc.perform(post("/api/desktop/purge").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"before\":\"" + clock.today() + "\"}"))
                .andExpect(status().isUnprocessableEntity());
    }
}
