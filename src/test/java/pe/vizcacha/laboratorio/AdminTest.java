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
import pe.vizcacha.laboratorio.service.ReportService;

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
class AdminTest {

    private static final String CABECERA_ID = "X-Record-Id";

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper json;

    private JsonNode getJson(String url) throws Exception {
        return leer(get(url));
    }

    private JsonNode leer(org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder peticion)
            throws Exception {
        byte[] cuerpo = mvc.perform(peticion).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        return json.readTree(new String(cuerpo, StandardCharsets.UTF_8));
    }

    private String postOkId(String url, Object body) throws Exception {
        return mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getHeader(CABECERA_ID);
    }

    private void post422(String url, Object body, String literal) throws Exception {
        mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(body)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value(literal));
    }

    private String primeraAccionAuditada() throws Exception {
        return getJson("/api/database").get("audit").get(0).get("action").asText();
    }

    private JsonNode reporte(String tipo, String rango) throws Exception {
        return leer(get("/api/reports").param("type", tipo).param("range", rango));
    }

    private String indicador(JsonNode reporte, String nombre) {
        for (JsonNode fila : reporte.get("rows")) {
            if (fila.get(0).asText().equals(nombre)) {
                return fila.get(1).asText();
            }
        }
        throw new IllegalStateException("Indicador ausente: " + nombre);
    }

    private Map<String, Object> examenNuevo() {
        Map<String, Object> e = new LinkedHashMap<>();
        e.put("name", "Hemoglobina glicosilada");
        e.put("category", "Bioquímica");
        e.put("price", 48.0);
        e.put("time", "24 horas");
        e.put("sample", "Sangre venosa");
        e.put("volume", "2 mL");
        e.put("prep", "No requiere ayuno.");
        e.put("equipment", "EQ-002");
        e.put("reagent", "REA-003");
        e.put("material", "MAT-002");
        e.put("enabled", true);
        e.put("format", "glucose");
        return e;
    }

    @Test
    void elExamenNuevoTomaElSiguienteCorrelativoYQuedaDisponible() throws Exception {
        String id = postOkId("/api/exams", examenNuevo());
        assertThat(id).isEqualTo("EX-000009");
        assertThat(primeraAccionAuditada()).isEqualTo("Examen creado");

        JsonNode db = getJson("/api/database");
        assertThat(db.get("exams")).hasSize(9);

        JsonNode nuevo = null;
        for (JsonNode e : db.get("exams")) {
            if (e.get("id").asText().equals(id)) {
                nuevo = e;
            }
        }
        assertThat(nuevo).isNotNull();
        assertThat(nuevo.get("name").asText()).isEqualTo("Hemoglobina glicosilada");
        assertThat(nuevo.get("format").asText()).isEqualTo("glucose");

        assertThat(nuevo.get("available").asBoolean()).isTrue();
    }

    @Test
    void deshabilitarUnExamenLoBloqueaPorSiMismo() throws Exception {
        Map<String, Object> e = examenNuevo();
        e.put("id", "EX-001");
        e.put("name", "Hemograma completo");
        e.put("equipment", "EQ-001");
        e.put("reagent", "REA-001");
        e.put("material", "MAT-001");
        e.put("format", "hemogram");
        e.put("enabled", false);

        assertThat(postOkId("/api/exams", e)).isEqualTo("EX-001");
        assertThat(primeraAccionAuditada()).isEqualTo("Examen actualizado");

        for (JsonNode x : getJson("/api/database").get("exams")) {
            if (x.get("id").asText().equals("EX-001")) {
                assertThat(x.get("available").asBoolean()).isFalse();
                assertThat(x.get("reasons").get(0).asText()).isEqualTo("Examen deshabilitado");
            }
        }
    }

    @Test
    void elTemaSeGuardaEnLaBaseYSoloAdmiteLosTres() throws Exception {
        assertThat(getJson("/api/database").get("appearance").asText()).isEqualTo("gris");

        postOkId("/api/settings/appearance", Map.of("theme", "negro"));
        assertThat(getJson("/api/database").get("appearance").asText()).isEqualTo("negro");

        post422("/api/settings/appearance", Map.of("theme", "turquesa"),
                "Tema no disponible: turquesa");
        assertThat(getJson("/api/database").get("appearance").asText()).isEqualTo("negro");

        postOkId("/api/settings/appearance", Map.of("theme", "gris"));
    }

    @Test
    void guardaLaConfiguracionGeneralYLaAudita() throws Exception {
        assertThat(postOkId("/api/settings",
                Map.of("lab", "Laboratorio Vizcacha", "branch", "Sede Andina")))
                .isEqualTo("General");

        JsonNode db = getJson("/api/database");
        assertThat(db.get("lab").asText()).isEqualTo("Laboratorio Vizcacha");
        assertThat(db.get("branch").asText()).isEqualTo("Sede Andina");
        assertThat(db.get("audit").get(0).get("action").asText())
                .isEqualTo("Configuración actualizada");
        assertThat(db.get("audit").get(0).get("record").asText()).isEqualTo("General");
    }

    @Test
    void alternaLasNotificacionesYLoAudita() throws Exception {
        assertThat(postOkId("/api/settings/notifications", Map.of("enabled", false)))
                .isEqualTo("Alertas");

        JsonNode db = getJson("/api/database");
        assertThat(db.get("notifications").asBoolean()).isFalse();
        assertThat(db.get("audit").get(0).get("action").asText())
                .isEqualTo("Notificaciones actualizadas");
        assertThat(db.get("audit").get(0).get("record").asText()).isEqualTo("Alertas");
    }

    @Test
    void elReporteDeActividadDeHoyCuadraConLaSemilla() throws Exception {
        JsonNode r = reporte(ReportService.ACTIVIDAD, ReportService.HOY);

        assertThat(r.get("columns")).hasSize(3);
        assertThat(r.get("columns").get(0).asText()).isEqualTo("Indicador");
        assertThat(r.get("rows")).hasSize(6);

        assertThat(indicador(r, "Pacientes registrados")).isEqualTo("0");
        assertThat(indicador(r, "Solicitudes registradas")).isEqualTo("5");
        assertThat(indicador(r, "Exámenes realizados")).isEqualTo("5");
        assertThat(indicador(r, "Muestras procesadas")).isEqualTo("7");
        assertThat(indicador(r, "Resultados publicados")).isEqualTo("2");
        assertThat(indicador(r, "Resultados pendientes")).isEqualTo("10");
    }

    @Test
    void losRangosAmplianElPeriodoCorrectamente() throws Exception {
        JsonNode semana = reporte(ReportService.ACTIVIDAD, ReportService.ESTA_SEMANA);
        assertThat(semana.get("from").asText()).isEqualTo("2026-09-21");
        assertThat(semana.get("to").asText()).isEqualTo("2026-09-24");

        assertThat(indicador(semana, "Solicitudes registradas")).isEqualTo("28");

        JsonNode mes = reporte(ReportService.ACTIVIDAD, ReportService.ESTE_MES);
        assertThat(mes.get("from").asText()).isEqualTo("2026-09-01");
        assertThat(indicador(mes, "Solicitudes registradas")).isEqualTo("35");

        assertThat(indicador(mes, "Pacientes registrados")).isEqualTo("5");
    }

    @Test
    void elRangoPersonalizadoRespetaLasDosFechas() throws Exception {
        JsonNode r = leer(get("/api/reports")
                .param("type", ReportService.ACTIVIDAD)
                .param("range", ReportService.PERSONALIZADO)
                .param("from", "2026-09-18")
                .param("to", "2026-09-19"));

        assertThat(r.get("from").asText()).isEqualTo("2026-09-18");
        assertThat(r.get("to").asText()).isEqualTo("2026-09-19");

        assertThat(indicador(r, "Solicitudes registradas")).isEqualTo("7");
    }

    @Test
    void elReporteDeExamenesMasSolicitadosVaEnOrdenDeCatalogo() throws Exception {
        JsonNode r = reporte(ReportService.MAS_SOLICITADOS, ReportService.HOY);

        assertThat(r.get("columns").get(2).asText()).isEqualTo("Solicitudes");
        assertThat(r.get("rows")).hasSize(8);
        assertThat(r.get("rows").get(0).get(0).asText()).isEqualTo("Hemograma completo");
        assertThat(r.get("rows").get(0).get(2).asText()).isEqualTo("5");
        assertThat(r.get("rows").get(1).get(2).asText()).isEqualTo("5");
        assertThat(r.get("rows").get(2).get(2).asText()).isEqualTo("1");
        assertThat(r.get("rows").get(3).get(2).asText()).isEqualTo("1");
        assertThat(r.get("rows").get(4).get(2).asText()).isEqualTo("0");
    }

    @Test
    void elReporteDeInventarioExcluyeLosEquipos() throws Exception {
        JsonNode r = reporte(ReportService.INVENTARIO, ReportService.HOY);

        assertThat(r.get("rows")).hasSize(13);
        assertThat(r.get("rows").get(0).get(0).asText()).isEqualTo("Reactivo hematológico");
        assertThat(r.get("rows").get(0).get(2).asText()).isEqualTo("85 pruebas");
        assertThat(r.get("rows").get(1).get(3).asText()).isEqualTo("Stock bajo");
    }

    @Test
    void elReporteDeEquiposEnMantenimientoSoloMuestraLosEnProceso() throws Exception {
        JsonNode r = reporte(ReportService.EN_MANTENIMIENTO, ReportService.HOY);

        assertThat(r.get("rows")).hasSize(1);
        assertThat(r.get("rows").get(0).get(0).asText()).isEqualTo("Analizador de inmunología");
        assertThat(r.get("rows").get(0).get(1).asText()).isEqualTo("Puma Montés");
        assertThat(r.get("rows").get(0).get(2).asText()).isEqualTo("30/09/2026");
        assertThat(r.get("rows").get(0).get(3).asText()).isEqualTo("En proceso");
    }

    @Test
    void elReporteDeConsumoLeeLaAuditoriaYExcluyeLasEntradas() throws Exception {

        assertThat(reporte(ReportService.CONSUMO_REACTIVOS, ReportService.HOY).get("rows"))
                .isEmpty();

        postOkId("/api/resources/REA-002/movements",
                Map.of("direction", "OUT", "qty", 3, "notes", "Control de calidad"));
        postOkId("/api/resources/REA-002/movements",
                Map.of("direction", "IN", "qty", 10, "notes", "Compra"));

        JsonNode r = reporte(ReportService.CONSUMO_REACTIVOS, ReportService.HOY);
        assertThat(r.get("rows")).hasSize(1);
        assertThat(r.get("rows").get(0).get(0).asText()).isEqualTo("24/09/2026");
        assertThat(r.get("rows").get(0).get(1).asText()).isEqualTo("REA-002");
        assertThat(r.get("rows").get(0).get(2).asText())
                .isEqualTo("Reactivo / material retirado · 3 pruebas · Control de calidad");
        assertThat(r.get("rows").get(0).get(3).asText()).isEqualTo("Responsable del laboratorio");
    }

    @Test
    void elAjusteDeInventarioApareceEnLosDosReportesDeConsumo() throws Exception {
        Map<String, Object> form = new LinkedHashMap<>();
        form.put("date", "2026-09-24");
        form.put("responsible", "Puma Montés");
        form.put("area", "Almacén A");
        form.put("kind", "Materiales");
        form.put("items", List.of(
                Map.of("resourceId", "MAT-001", "counted", "15", "observation", "Rotura")));
        postOkId("/api/inventory/count", form);

        assertThat(reporte(ReportService.CONSUMO_MATERIALES, ReportService.HOY).get("rows"))
                .hasSize(1);
        assertThat(reporte(ReportService.CONSUMO_REACTIVOS, ReportService.HOY).get("rows"))
                .hasSize(1);
    }
}
