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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AlpacasTest {

    private static final String CABECERA_ID = "X-Record-Id";

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper json;

    private JsonNode snapshot() throws Exception {
        byte[] cuerpo = mvc.perform(get("/api/database")).andReturn()
                .getResponse().getContentAsByteArray();
        return json.readTree(new String(cuerpo, StandardCharsets.UTF_8));
    }

    private JsonNode buscar(String coleccion, String id) throws Exception {
        for (JsonNode n : snapshot().get(coleccion)) {
            if (n.get("id").asText().equals(id)) {
                return n;
            }
        }
        throw new IllegalStateException("No encontrado en " + coleccion + ": " + id);
    }

    private String postOkId(String url, Object body) throws Exception {
        return mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getHeader(CABECERA_ID);
    }

    private void post422(String url, Object body, String mensajeLiteral) throws Exception {
        mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(body)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value(mensajeLiteral));
    }

    private Map<String, Object> mantenimiento(String id, String equipo, String estado,
                                              String inicio, String fin, String real) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", id);
        m.put("equipment", equipo);
        m.put("type", "Preventivo");
        m.put("start", inicio);
        m.put("end", fin);
        m.put("actual", real);
        m.put("responsible", "Puma Montés");
        m.put("provider", "Servicio técnico Demo");
        m.put("description", "Revisión programada.");
        m.put("state", estado);
        return m;
    }

    private String primeraAccionAuditada() throws Exception {
        return snapshot().get("audit").get(0).get("action").asText();
    }

    private List<String> motivos(String examId) throws Exception {
        List<String> lista = new java.util.ArrayList<>();
        buscar("exams", examId).get("reasons").forEach(n -> lista.add(n.asText()));
        return lista;
    }

    private boolean disponible(String examId) throws Exception {
        return buscar("exams", examId).get("available").asBoolean();
    }

    private long alertasDe(String titulo) throws Exception {
        long n = 0;
        for (JsonNode a : snapshot().get("alerts")) {
            if (a.get("title").asText().equals(titulo)) {
                n++;
            }
        }
        return n;
    }

    @Test
    void elMantenimientoEnProcesoBloqueaLosExamenesDelEquipoYSeRevierteAlFinalizar()
            throws Exception {

        assertThat(disponible("EX-001")).isTrue();
        assertThat(buscar("resources", "EQ-001").get("status").asText()).isEqualTo("OPERATIVO");
        assertThat(alertasDe("Analizador hematológico")).isZero();

        String manId = postOkId("/api/maintenance", mantenimiento(
                null, "EQ-001", "En proceso", "2026-09-24", "2026-09-28", ""));
        assertThat(manId).isEqualTo("MAN-000002");

        JsonNode equipo = buscar("resources", "EQ-001");
        assertThat(equipo.get("status").asText()).isEqualTo("EN MANTENIMIENTO");
        assertThat(equipo.get("derivedStatus").asText()).isEqualTo("EN MANTENIMIENTO");
        assertThat(equipo.get("next").asText()).isEqualTo("2026-09-28");

        assertThat(disponible("EX-001")).isFalse();
        assertThat(motivos("EX-001"))
                .containsExactly("Analizador hematológico: equipo en mantenimiento");

        assertThat(alertasDe("Analizador hematológico")).isEqualTo(1);

        post422("/api/samples",
                Map.of("requestId", "SOL-000003", "examId", "EX-001",
                        "takenAt", "2026-09-24T15:00", "responsible", "Vicuña Serrana"),
                "No se puede registrar la toma: Analizador hematológico: equipo en "
                        + "mantenimiento. La solicitud permanece pendiente.");

        post422("/api/samples/MUE-2026-000001/state", Map.of("state", "EN ANÁLISIS"),
                "No se puede iniciar el análisis: Analizador hematológico: "
                        + "equipo en mantenimiento");

        postOkId("/api/maintenance", mantenimiento(
                manId, "EQ-001", "Finalizado", "2026-09-24", "2026-09-28", "2026-09-26"));

        JsonNode restablecido = buscar("resources", "EQ-001");
        assertThat(restablecido.get("status").asText()).isEqualTo("OPERATIVO");
        assertThat(restablecido.get("last").asText()).isEqualTo("2026-09-26");
        assertThat(disponible("EX-001")).isTrue();
        assertThat(alertasDe("Analizador hematológico")).isZero();
    }

    @Test
    void cambiarDeEquipoRecalculaTambienElEquipoAnterior() throws Exception {
        String manId = postOkId("/api/maintenance", mantenimiento(
                null, "EQ-001", "En proceso", "2026-09-24", "2026-09-28", ""));
        assertThat(buscar("resources", "EQ-001").get("status").asText())
                .isEqualTo("EN MANTENIMIENTO");

        postOkId("/api/maintenance", mantenimiento(
                manId, "EQ-002", "En proceso", "2026-09-24", "2026-09-28", ""));

        assertThat(buscar("resources", "EQ-001").get("status").asText()).isEqualTo("OPERATIVO");
        assertThat(buscar("resources", "EQ-002").get("status").asText())
                .isEqualTo("EN MANTENIMIENTO");

        assertThat(disponible("EX-001")).isTrue();
        assertThat(disponible("EX-002")).isFalse();
        assertThat(disponible("EX-003")).isFalse();
    }

    @Test
    void unMantenimientoProgramadoAdelantaLaProximaIntervencion() throws Exception {
        postOkId("/api/maintenance", mantenimiento(
                null, "EQ-004", "Programado", "2026-10-15", "2026-10-16", ""));

        JsonNode equipo = buscar("resources", "EQ-004");
        assertThat(equipo.get("next").asText()).isEqualTo("2026-10-15");

        assertThat(equipo.get("status").asText()).isEqualTo("OPERATIVO");
        assertThat(disponible("EX-004")).isTrue();
    }

    @Test
    void validaLasFechasYElEstadoDelMantenimiento() throws Exception {
        post422("/api/maintenance",
                mantenimiento(null, "EQ-001", "Programado", "2026-09-24", "2026-09-20", ""),
                "La finalización no puede ser anterior al inicio.");

        post422("/api/maintenance",
                mantenimiento(null, "EQ-001", "Programado", "2026-09-24", "2026-09-28",
                        "2026-09-20"),
                "La finalización no puede ser anterior al inicio.");

        post422("/api/maintenance",
                mantenimiento(null, "EQ-001", "Finalizado", "2026-09-24", "2026-09-28", ""),
                "Registra la fecha real de finalización.");
    }

    @Test
    void unEquipoNoPuedeTenerDosMantenimientosEnProceso() throws Exception {
        post422("/api/maintenance",
                mantenimiento(null, "EQ-003", "En proceso", "2026-09-24", "2026-09-28", ""),
                "Este equipo ya tiene un mantenimiento en proceso.");
    }

    @Test
    void editarElMantenimientoEnProcesoNoChocaConSigoMismo() throws Exception {
        String id = postOkId("/api/maintenance", mantenimiento(
                "MAN-000001", "EQ-003", "En proceso", "2026-09-20", "2026-10-05", ""));

        assertThat(id).isEqualTo("MAN-000001");
        assertThat(buscar("resources", "EQ-003").get("next").asText()).isEqualTo("2026-10-05");
    }

    @Test
    void noSePuedeDeclararOperativoUnEquipoConMantenimientoEnProceso() throws Exception {
        Map<String, Object> eq = new LinkedHashMap<>();
        eq.put("id", "EQ-003");
        eq.put("kind", "Equipos");
        eq.put("name", "Analizador de inmunología");
        eq.put("area", "Inmunología");
        eq.put("status", "OPERATIVO");

        post422("/api/resources", eq,
                "Finaliza primero el mantenimiento en proceso para habilitar este equipo.");
    }

    @Test
    void elIdDeUnRecursoNuevoSeCalculaSobreTodasLasFamilias() throws Exception {
        Map<String, Object> eq = new LinkedHashMap<>();
        eq.put("kind", "Equipos");
        eq.put("name", "Centrífuga de demostración");
        eq.put("area", "Laboratorio Central");
        eq.put("qty", 0);
        eq.put("min", 10);

        assertThat(postOkId("/api/resources", eq)).isEqualTo("EQ-000008");
        assertThat(primeraAccionAuditada()).isEqualTo("Recurso agregado");

        eq.put("kind", "Reactivos");
        eq.put("name", "Reactivo de demostración");
        assertThat(postOkId("/api/resources", eq)).isEqualTo("REA-000009");
    }

    @Test
    void elRecursoNuevoHeredaLosDefaultsDeUnidadEstadoYResponsable() throws Exception {
        Map<String, Object> mat = new LinkedHashMap<>();
        mat.put("kind", "Materiales");
        mat.put("name", "Gradilla de demostración");
        mat.put("area", "Almacén A");
        mat.put("qty", 40);
        mat.put("min", 10);

        String id = postOkId("/api/resources", mat);
        JsonNode r = buscar("resources", id);

        assertThat(r.get("unit").asText()).isEqualTo("unidades");
        assertThat(r.get("status").asText()).isEqualTo("OPERATIVO");
        assertThat(r.get("responsible").asText()).isEqualTo("Responsable del laboratorio");
        assertThat(r.get("derivedStatus").asText()).isEqualTo("Disponible");
    }

    @Test
    void laSalidaQueAgotaUnReactivoBloqueaSuExamenYLaEntradaLoRestablece()
            throws Exception {
        assertThat(disponible("EX-002")).isTrue();
        assertThat(buscar("resources", "REA-002").get("derivedStatus").asText())
                .isEqualTo("Stock bajo");

        postOkId("/api/resources/REA-002/movements",
                Map.of("direction", "OUT", "qty", 12, "notes", "Consumo de demostración"));

        JsonNode agotado = buscar("resources", "REA-002");
        assertThat(agotado.get("qty").asInt()).isZero();
        assertThat(agotado.get("derivedStatus").asText()).isEqualTo("Agotado");

        assertThat(disponible("EX-002")).isFalse();
        assertThat(motivos("EX-002"))
                .containsExactly("Reactivo de glucosa: reactivo sin stock");

        postOkId("/api/resources/REA-002/movements",
                Map.of("direction", "IN", "qty", 30, "notes", "Reposición de demostración"));

        assertThat(buscar("resources", "REA-002").get("qty").asInt()).isEqualTo(30);
        assertThat(buscar("resources", "REA-002").get("derivedStatus").asText())
                .isEqualTo("Disponible");
        assertThat(disponible("EX-002")).isTrue();
    }

    @Test
    void rechazaCantidadesNoPositivasOSalidasQueSuperanElStock() throws Exception {
        String literal = "La cantidad debe ser positiva y no superar el stock "
                + "disponible para una salida.";

        post422("/api/resources/REA-002/movements",
                Map.of("direction", "OUT", "qty", 0, "notes", "x"), literal);
        post422("/api/resources/REA-002/movements",
                Map.of("direction", "OUT", "qty", -3, "notes", "x"), literal);
        post422("/api/resources/REA-002/movements",
                Map.of("direction", "OUT", "qty", 13, "notes", "x"), literal);

        postOkId("/api/resources/REA-002/movements",
                Map.of("direction", "IN", "qty", 999, "notes", "Compra anual"));
        assertThat(buscar("resources", "REA-002").get("qty").asInt()).isEqualTo(1011);
    }

    @Test
    void elMovimientoQuedaAuditadoConCantidadUnidadYMotivo() throws Exception {
        postOkId("/api/resources/REA-002/movements",
                Map.of("direction", "OUT", "qty", 4, "notes", "Control de calidad"));

        JsonNode entrada = snapshot().get("audit").get(0);
        assertThat(entrada.get("action").asText()).isEqualTo(
                "Reactivo / material retirado · 4 pruebas · Control de calidad");
        assertThat(entrada.get("module").asText()).isEqualTo("Reactivos");
        assertThat(entrada.get("record").asText()).isEqualTo("REA-002");

        postOkId("/api/resources/MAT-001/movements",
                Map.of("direction", "IN", "qty", 50, "notes", "Compra"));
        assertThat(primeraAccionAuditada()).isEqualTo(
                "Reactivo / material ingresado · 50 unidades · Compra");
    }

    @Test
    void elInventarioFisicoSoloAjustaLosRecursosContados() throws Exception {
        Map<String, Object> form = new LinkedHashMap<>();
        form.put("date", "2026-09-24");
        form.put("responsible", "Puma Montés");
        form.put("area", "Almacén A");
        form.put("kind", "Materiales");
        form.put("items", List.of(
                Map.of("resourceId", "MAT-001", "counted", "20", "observation", "Recuento OK"),
                Map.of("resourceId", "MAT-002", "counted", "", "observation", ""),
                Map.of("resourceId", "MAT-005", "counted", "480", "observation", "Faltan cajas")));

        assertThat(postOkId("/api/inventory/count", form)).isEqualTo("2");

        assertThat(buscar("resources", "MAT-001").get("qty").asInt()).isEqualTo(20);
        assertThat(buscar("resources", "MAT-005").get("qty").asInt()).isEqualTo(480);

        assertThat(buscar("resources", "MAT-002").get("qty").asInt()).isEqualTo(150);
    }

    @Test
    void elInventarioFisicoAuditaLaDiferenciaDeCadaRecurso() throws Exception {
        Map<String, Object> form = new LinkedHashMap<>();
        form.put("date", "2026-09-24");
        form.put("responsible", "Puma Montés");
        form.put("area", "Almacén A");
        form.put("kind", "Materiales");
        form.put("items", List.of(
                Map.of("resourceId", "MAT-001", "counted", "15", "observation", "Rotura")));

        postOkId("/api/inventory/count", form);

        assertThat(primeraAccionAuditada()).isEqualTo(
                "Inventario físico ajustado · 18 → 15 (-3) · 2026-09-24 · Almacén A "
                        + "· Responsable: Puma Montés · Rotura");
    }

    @Test
    void elInventarioFisicoExigeAlMenosUnaCantidadYQueSeanEnteras() throws Exception {
        Map<String, Object> form = new LinkedHashMap<>();
        form.put("date", "2026-09-24");
        form.put("responsible", "Puma Montés");
        form.put("area", "Almacén A");
        form.put("kind", "Materiales");
        form.put("items", List.of(
                Map.of("resourceId", "MAT-001", "counted", "", "observation", "")));

        post422("/api/inventory/count", form, "Registra al menos una cantidad física.");

        form.put("items", List.of(
                Map.of("resourceId", "MAT-001", "counted", "12.5", "observation", "")));
        post422("/api/inventory/count", form,
                "Las cantidades deben ser enteras y no negativas.");

        form.put("items", List.of(
                Map.of("resourceId", "MAT-001", "counted", "-4", "observation", "")));
        post422("/api/inventory/count", form,
                "Las cantidades deben ser enteras y no negativas.");
    }

    @Test
    void unaLineaInvalidaCancelaElAjusteCompleto() throws Exception {
        Map<String, Object> form = new LinkedHashMap<>();
        form.put("date", "2026-09-24");
        form.put("responsible", "Puma Montés");
        form.put("area", "Almacén A");
        form.put("kind", "Materiales");
        form.put("items", List.of(
                Map.of("resourceId", "MAT-001", "counted", "20", "observation", ""),
                Map.of("resourceId", "MAT-005", "counted", "abc", "observation", "")));

        post422("/api/inventory/count", form,
                "Las cantidades deben ser enteras y no negativas.");

        assertThat(buscar("resources", "MAT-001").get("qty").asInt()).isEqualTo(18);
    }

    @Test
    void elSnapshotTraeDisponibilidadEstadoDerivadoYAlertas() throws Exception {
        JsonNode db = snapshot();

        assertThat(db.get("alerts")).hasSize(8);

        long disponibles = 0;
        for (JsonNode e : db.get("exams")) {
            assertThat(e.has("available")).isTrue();
            assertThat(e.has("reasons")).isTrue();
            if (e.get("available").asBoolean()) {
                disponibles++;
            }
        }
        assertThat(disponibles).isEqualTo(4);

        for (JsonNode r : db.get("resources")) {
            assertThat(r.get("derivedStatus").asText()).isNotBlank();
        }
    }
}
