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

/**
 * Entregable de la Fase 5: los acoplamientos entre modulos, verificados por
 * API. Es el segundo guion de la exposicion (§10) y lo que demuestra que el
 * sistema esta conectado de verdad.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AlpacasTest {

    private static final String CABECERA_ID = "X-Record-Id";

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper json;

    // ------------------------------------------------------------ utilidades

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

    // ---------------------------- EL ENTREGABLE: la cascada del mantenimiento

    /**
     * Guion A de §10: mantenimiento En proceso sobre EQ-001, el equipo cambia
     * solo, el hemograma se bloquea, aparece la alerta, y al finalizarlo todo
     * vuelve atras.
     */
    @Test
    void elMantenimientoEnProcesoBloqueaLosExamenesDelEquipoYSeRevierteAlFinalizar()
            throws Exception {
        // Punto de partida: hemograma disponible, equipo operativo.
        assertThat(disponible("EX-001")).isTrue();
        assertThat(buscar("resources", "EQ-001").get("status").asText()).isEqualTo("OPERATIVO");
        assertThat(alertasDe("Analizador hematológico")).isZero();

        // A. Mantenimiento En proceso sobre el analizador hematologico.
        String manId = postOkId("/api/maintenance", mantenimiento(
                null, "EQ-001", "En proceso", "2026-09-24", "2026-09-28", ""));
        assertThat(manId).isEqualTo("MAN-000002");

        // El equipo cambia solo, y su proxima intervencion toma la fecha de fin.
        JsonNode equipo = buscar("resources", "EQ-001");
        assertThat(equipo.get("status").asText()).isEqualTo("EN MANTENIMIENTO");
        assertThat(equipo.get("derivedStatus").asText()).isEqualTo("EN MANTENIMIENTO");
        assertThat(equipo.get("next").asText()).isEqualTo("2026-09-28");

        // El hemograma se bloquea sin que nadie lo toque, con el motivo exacto.
        assertThat(disponible("EX-001")).isFalse();
        assertThat(motivos("EX-001"))
                .containsExactly("Analizador hematológico: equipo en mantenimiento");

        // Y aparece la alerta operativa.
        assertThat(alertasDe("Analizador hematológico")).isEqualTo(1);

        // Ya no se puede registrar una toma de hemograma...
        post422("/api/samples",
                Map.of("requestId", "SOL-000003", "examId", "EX-001",
                        "takenAt", "2026-09-24T15:00", "responsible", "Vicuña Serrana"),
                "No se puede registrar la toma: Analizador hematológico: equipo en "
                        + "mantenimiento. La solicitud permanece pendiente.");

        // ...ni enviar a analisis una que ya estaba registrada.
        post422("/api/samples/MUE-2026-000001/state", Map.of("state", "EN ANÁLISIS"),
                "No se puede iniciar el análisis: Analizador hematológico: "
                        + "equipo en mantenimiento");

        // C. Finalizarlo revierte la cadena entera.
        postOkId("/api/maintenance", mantenimiento(
                manId, "EQ-001", "Finalizado", "2026-09-24", "2026-09-28", "2026-09-26"));

        JsonNode restablecido = buscar("resources", "EQ-001");
        assertThat(restablecido.get("status").asText()).isEqualTo("OPERATIVO");
        assertThat(restablecido.get("last").asText()).isEqualTo("2026-09-26");
        assertThat(disponible("EX-001")).isTrue();
        assertThat(alertasDe("Analizador hematológico")).isZero();
    }

    /**
     * El caso que es facil olvidar: si el mantenimiento cambia de equipo, el
     * que lo tenia antes tambien tiene que recalcularse.
     */
    @Test
    void cambiarDeEquipoRecalculaTambienElEquipoAnterior() throws Exception {
        String manId = postOkId("/api/maintenance", mantenimiento(
                null, "EQ-001", "En proceso", "2026-09-24", "2026-09-28", ""));
        assertThat(buscar("resources", "EQ-001").get("status").asText())
                .isEqualTo("EN MANTENIMIENTO");

        // El mismo mantenimiento pasa a referirse a EQ-002.
        postOkId("/api/maintenance", mantenimiento(
                manId, "EQ-002", "En proceso", "2026-09-24", "2026-09-28", ""));

        assertThat(buscar("resources", "EQ-001").get("status").asText()).isEqualTo("OPERATIVO");
        assertThat(buscar("resources", "EQ-002").get("status").asText())
                .isEqualTo("EN MANTENIMIENTO");

        // Y la disponibilidad sigue a los equipos: hemograma vuelve, los de
        // bioquimica se van.
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
        // Programado no bloquea: solo En proceso lo hace.
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

    /** EQ-003 ya tiene MAN-000001 en proceso desde la semilla. */
    @Test
    void unEquipoNoPuedeTenerDosMantenimientosEnProceso() throws Exception {
        post422("/api/maintenance",
                mantenimiento(null, "EQ-003", "En proceso", "2026-09-24", "2026-09-28", ""),
                "Este equipo ya tiene un mantenimiento en proceso.");
    }

    /** Editar el mismo mantenimiento sin cambiar de estado no choca consigo. */
    @Test
    void editarElMantenimientoEnProcesoNoChocaConSigoMismo() throws Exception {
        String id = postOkId("/api/maintenance", mantenimiento(
                "MAN-000001", "EQ-003", "En proceso", "2026-09-20", "2026-10-05", ""));

        assertThat(id).isEqualTo("MAN-000001");
        assertThat(buscar("resources", "EQ-003").get("next").asText()).isEqualTo("2026-10-05");
    }

    // ---------------------------------------------------- recursos §6.4.10

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

    /**
     * El prefijo lo da la familia, pero el correlativo se calcula sobre todos
     * los recursos: con la semilla el sufijo mas alto es 7, de MAT-007.
     * Ademas la semilla usa 3 digitos y el generador 6: es una inconsistencia
     * del original que se replica a proposito.
     */
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

        // El siguiente ya cuenta con el anterior.
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

    // ----------------------------------------- movimientos de stock §6.4.7

    /** Guion B de §10: vaciar el reactivo de glucosa bloquea su examen. */
    @Test
    void laSalidaQueAgotaUnReactivoBloqueaSuExamenYLaEntradaLoRestablece()
            throws Exception {
        assertThat(disponible("EX-002")).isTrue();
        assertThat(buscar("resources", "REA-002").get("derivedStatus").asText())
                .isEqualTo("Stock bajo");

        // Salida de las 12 unidades.
        postOkId("/api/resources/REA-002/movements",
                Map.of("direction", "OUT", "qty", 12, "notes", "Consumo de demostración"));

        JsonNode agotado = buscar("resources", "REA-002");
        assertThat(agotado.get("qty").asInt()).isZero();
        assertThat(agotado.get("derivedStatus").asText()).isEqualTo("Agotado");

        assertThat(disponible("EX-002")).isFalse();
        assertThat(motivos("EX-002"))
                .containsExactly("Reactivo de glucosa: reactivo sin stock");

        // Reponer lo devuelve a disponible.
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

        // Una entrada no tiene techo.
        postOkId("/api/resources/REA-002/movements",
                Map.of("direction", "IN", "qty", 999, "notes", "Compra anual"));
        assertThat(buscar("resources", "REA-002").get("qty").asInt()).isEqualTo(1011);
    }

    /** La accion auditada lleva cantidad, unidad y motivo: de ahi salen los
     *  reportes de consumo (§9.12). */
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

    // ------------------------------------------ inventario fisico §6.4.8

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
        // El que iba en blanco no se toca.
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

    /** Si una linea es invalida no se aplica ninguna: todo o nada. */
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

    // ------------------------------------------- campos calculados §11.4

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
