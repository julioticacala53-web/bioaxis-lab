package pe.vizcacha.laboratorio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.repo.AlpacaRepository;

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
 * Entregable de la Fase 4: el guion de demostracion de §10 ejecutado de
 * principio a fin por HTTP, y las validaciones de §6.4 con sus mensajes
 * literales.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FlowTest {

    private static final String CABECERA_ID = "X-Record-Id";

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper json;
    @Autowired private pe.vizcacha.laboratorio.service.SeedService seed;
    @Autowired private AlpacaRepository resources;
    @jakarta.persistence.PersistenceContext private jakarta.persistence.EntityManager em;

    // ------------------------------------------------------------- utilidades

    private MvcResult postOk(String url, Object body) throws Exception {
        return mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn();
    }

    private String postOkId(String url, Object body) throws Exception {
        return postOk(url, body).getResponse().getHeader(CABECERA_ID);
    }

    private void post422(String url, Object body, String mensajeLiteral) throws Exception {
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(body)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value(mensajeLiteral));
    }

    private Map<String, Object> pacienteMaria() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("dni", "87654321");
        m.put("first", "Chinchilla");
        m.put("last", "Condori Huanca");
        m.put("birth", "1994-04-12");
        m.put("sex", "Femenino");
        m.put("phone", "900111222");
        m.put("email", "chinchilla.demo@email.com");
        m.put("address", "Av. Laboratorio 100, Ciudad Demo");
        m.put("notes", "");
        return m;
    }

    /** Los 9 parametros del hemograma, todos dentro de rango. */
    private Map<String, String> hemogramaCompleto() {
        Map<String, String> v = new LinkedHashMap<>();
        v.put("Hemoglobina", "13.8");
        v.put("Hematocrito", "40.5");
        v.put("Leucocitos", "6.7");
        v.put("Plaquetas", "262.5");
        v.put("Neutrófilos", "53.5");
        v.put("Linfocitos", "31.25");
        v.put("Monocitos", "5.6");
        v.put("Eosinófilos", "2.7");
        v.put("Basófilos", "0.9");
        return v;
    }

    // ------------------------------------------- el guion completo de la §10

    @Test
    void ejecutaElGuionDeDemostracionDePrincipioAFin() throws Exception {
        // 1. Registrar paciente
        String pacienteId = postOkId("/api/patients", pacienteMaria());
        assertThat(pacienteId).isEqualTo("PAC-000006");

        // 2. Crear solicitud con Hemograma + Glucosa
        String solicitudId = postOkId("/api/requests", Map.of(
                "patientId", pacienteId,
                "examIds", List.of("EX-001", "EX-002"),
                "allowPending", false));
        assertThat(solicitudId).isEqualTo("SOL-000036");

        // 3. La solicitud no queda pendiente: los recursos estan disponibles
        assertThat(pendiente(solicitudId)).isFalse();

        int tubosAntes = qty("MAT-001");
        int reactivoAntes = qty("REA-001");

        // 4. Registrar muestra
        String muestraId = postOkId("/api/samples", Map.of(
                "requestId", solicitudId,
                "examId", "EX-001",
                "takenAt", "2026-09-24T14:30",
                "responsible", "Vicuña Serrana"));
        assertThat(muestraId).isEqualTo("MUE-2026-000043");

        // Descuenta exactamente una unidad de reactivo y una de material
        assertThat(qty("REA-001")).isEqualTo(reactivoAntes - 1);
        assertThat(qty("MAT-001")).isEqualTo(tubosAntes - 1);

        // 5. Estado inicial RECIBIDA, con su primer paso de trazabilidad
        assertThat(estado(muestraId)).isEqualTo("RECIBIDA");
        JsonNode recienCreada = buscar("samples", muestraId);
        assertThat(recienCreada.get("history")).hasSize(1);
        assertThat(recienCreada.get("history").get(0).get("state").asText())
                .isEqualTo("RECIBIDA");

        // 6. Enviar a analisis
        postOk("/api/samples/" + muestraId + "/state", Map.of("state", "EN ANÁLISIS"));
        assertThat(estado(muestraId)).isEqualTo("EN ANÁLISIS");

        // 7 y 8. Ingresar resultado y enviar a validacion
        postOk("/api/samples/" + muestraId + "/results", Map.of(
                "values", hemogramaCompleto(),
                "notes", "Sin hallazgos relevantes.",
                "submit", true));
        assertThat(estado(muestraId)).isEqualTo("EN VALIDACIÓN");

        // 9. Validar
        postOk("/api/samples/" + muestraId + "/state", Map.of("state", "VALIDADO"));
        assertThat(estado(muestraId)).isEqualTo("VALIDADO");

        // 10. Publicar
        postOk("/api/samples/" + muestraId + "/state", Map.of("state", "PUBLICADA"));
        assertThat(estado(muestraId)).isEqualTo("PUBLICADA");

        // 11. En la ficha del paciente el resultado aparece publicado y con
        //     sus valores, y la trazabilidad tiene los 5 pasos firmados.
        JsonNode publicada = buscar("samples", muestraId);
        assertThat(publicada.get("patient").asText()).isEqualTo(pacienteId);
        assertThat(publicada.get("state").asText()).isEqualTo("PUBLICADA");
        assertThat(publicada.get("results").get("Hemoglobina").asText()).isEqualTo("13.8");
        assertThat(publicada.get("notes").asText()).isEqualTo("Sin hallazgos relevantes.");
        assertThat(publicada.get("history")).hasSize(5);
        assertThat(publicada.get("history").get(4).get("state").asText()).isEqualTo("PUBLICADA");

        // Toda la secuencia quedo auditada, lo mas nuevo primero.
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.audit[0].action").value("Resultado publicado"))
                .andExpect(jsonPath("$.audit[0].record").value(muestraId))
                .andExpect(jsonPath("$.audit[1].action").value("Resultado validado"))
                .andExpect(jsonPath("$.audit[2].action").value("Resultado ingresado"))
                .andExpect(jsonPath("$.audit[3].action").value("Muestra enviada a análisis"))
                .andExpect(jsonPath("$.audit[4].action").value("Muestra registrada"))
                .andExpect(jsonPath("$.audit[5].action").value("Solicitud creada"))
                .andExpect(jsonPath("$.audit[6].action").value("Paciente creado"));
    }

    /**
     * El snapshot, decodificado explicitamente como UTF-8.
     *
     * getContentAsString() sin charset usa ISO-8859-1 porque la respuesta
     * declara application/json sin charset -- que es correcto, JSON es UTF-8
     * por especificacion. Sin esto, "EN ANÁLISIS" se lee "EN ANÃLISIS" y
     * cualquier comparacion con tildes falla por un motivo que no existe.
     */
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

    private int qty(String resourceId) throws Exception {
        return buscar("resources", resourceId).get("qty").asInt();
    }

    private String estado(String sampleId) throws Exception {
        return buscar("samples", sampleId).get("state").asText();
    }

    private boolean pendiente(String requestId) throws Exception {
        return buscar("requests", requestId).get("pending").asBoolean();
    }

    // ------------------------------------------------------ pacientes §6.4.6

    @Test
    void rechazaUnDniQueNoTengaOchoDigitos() throws Exception {
        Map<String, Object> m = pacienteMaria();
        m.put("dni", "1234567");
        post422("/api/patients", m, "El DNI debe tener exactamente 8 dígitos.");

        m.put("dni", "1234567a");
        post422("/api/patients", m, "El DNI debe tener exactamente 8 dígitos.");
    }

    @Test
    void rechazaUnDniYaRegistrado() throws Exception {
        Map<String, Object> m = pacienteMaria();
        m.put("dni", "12345678");          // el de Alpaca Blanca Suri
        post422("/api/patients", m,
                "Ya existe un paciente con este DNI. Puedes consultarlo en Pacientes.");
    }

    @Test
    void editarUnPacienteConservaSuDniYSuFechaDeRegistro() throws Exception {
        Map<String, Object> m = pacienteMaria();
        m.put("id", "PAC-000001");
        m.put("dni", "12345678");
        m.put("first", "Alpaca Suri");

        String id = postOkId("/api/patients", m);
        assertThat(id).isEqualTo("PAC-000001");

        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.patients[0].first").value("Alpaca Suri"))
                .andExpect(jsonPath("$.patients[0].created").value("2026-09-20"))
                .andExpect(jsonPath("$.audit[0].action").value("Paciente actualizado"));
    }

    // ---------------------------------------------------- solicitudes §6.4.2

    @Test
    void rechazaUnaSolicitudSinPacienteOSinExamenes() throws Exception {
        post422("/api/requests",
                Map.of("patientId", "", "examIds", List.of(), "allowPending", false),
                "Selecciona un paciente y al menos un examen.");

        post422("/api/requests",
                Map.of("patientId", "PAC-000001", "examIds", List.of(), "allowPending", false),
                "Selecciona un paciente y al menos un examen.");
    }

    @Test
    void unExamenBloqueadoExigeConfirmarElRegistroPendiente() throws Exception {
        post422("/api/requests",
                Map.of("patientId", "PAC-000001", "examIds", List.of("EX-005"),
                        "allowPending", false),
                "Confirma el registro pendiente de disponibilidad para continuar.");

        // Con la casilla marcada se acepta, y queda pendiente.
        String id = postOkId("/api/requests",
                Map.of("patientId", "PAC-000001", "examIds", List.of("EX-005"),
                        "allowPending", true));

        assertThat(pendiente(id)).isTrue();
    }

    // -------------------------------------------------------- muestras §6.4.3

    @Test
    void noSePuedeRegistrarLaTomaDeUnExamenBloqueado() throws Exception {
        String solicitud = postOkId("/api/requests",
                Map.of("patientId", "PAC-000001", "examIds", List.of("EX-007"),
                        "allowPending", true));

        post422("/api/samples",
                Map.of("requestId", solicitud, "examId", "EX-007",
                        "takenAt", "2026-09-24T14:30", "responsible", "Vicuña Serrana"),
                "No se puede registrar la toma: Hisopos estériles: material insuficiente. "
                        + "La solicitud permanece pendiente.");
    }

    @Test
    void unExamenNoPuedeTenerDosMuestrasEnLaMismaSolicitud() throws Exception {
        Map<String, Object> toma = Map.of("requestId", "SOL-000001", "examId", "EX-001",
                "takenAt", "2026-09-24T14:30", "responsible", "Vicuña Serrana");

        post422("/api/samples", toma,
                "Este examen ya tiene una muestra registrada en la solicitud.");
    }

    @Test
    void rechazaUnaTomaSinSolicitudOSinExamen() throws Exception {
        post422("/api/samples",
                Map.of("requestId", "", "examId", "EX-001",
                        "takenAt", "2026-09-24T14:30", "responsible", "x"),
                "Selecciona una solicitud y un examen.");
    }

    /** El descuento se aplica antes de recalcular, asi que dejar un recurso
     *  en cero marca la solicitud como pendiente en el mismo movimiento. */
    @Test
    void elDescuentoQueAgotaUnRecursoDejaLaSolicitudPendiente() throws Exception {
        // Deja el reactivo de glucosa en 1: la toma lo pondra en 0.
        seed.reset();
        for (int i = 0; i < 11; i++) {
            postOk("/api/samples", Map.of(
                    "requestId", nuevaSolicitudGlucosa(), "examId", "EX-002",
                    "takenAt", "2026-09-24T14:30", "responsible", "Vicuña Serrana"));
        }
        assertThat(qty("REA-002")).isEqualTo(1);

        String ultima = nuevaSolicitudGlucosa();
        postOk("/api/samples", Map.of("requestId", ultima, "examId", "EX-002",
                "takenAt", "2026-09-24T14:30", "responsible", "Vicuña Serrana"));

        assertThat(qty("REA-002")).isZero();
        assertThat(pendiente(ultima)).isTrue();
    }

    private String nuevaSolicitudGlucosa() throws Exception {
        return postOkId("/api/requests", Map.of(
                "patientId", "PAC-000001", "examIds", List.of("EX-002"),
                "allowPending", true));
    }

    // ------------------------------------------------- transiciones §6.4.1/5

    /**
     * §6.4.1: la disponibilidad se vuelve a comprobar en el momento de enviar
     * a analisis, no solo al registrar la toma. Una muestra ya registrada
     * puede quedar bloqueada si su equipo entra en mantenimiento o su
     * reactivo se agota entre medias.
     *
     * El stock se toca por el repositorio porque los movimientos de
     * inventario son de la Fase 5 y todavia no hay endpoint.
     */
    @Test
    void noSePuedeIniciarElAnalisisSiElExamenSeBloqueoDespues() throws Exception {
        // MUE-2026-000001 esta RECIBIDA y es un hemograma (EX-001).
        assertThat(estado("MUE-2026-000001")).isEqualTo("RECIBIDA");

        // El reactivo hematologico se agota mientras la muestra espera.
        resources.findById("REA-001").orElseThrow().setQty(0);

        post422("/api/samples/MUE-2026-000001/state", Map.of("state", "EN ANÁLISIS"),
                "No se puede iniciar el análisis: Reactivo hematológico: reactivo sin stock");

        // Y sigue intacta: una transicion rechazada no modifica nada.
        assertThat(estado("MUE-2026-000001")).isEqualTo("RECIBIDA");
        assertThat(buscar("samples", "MUE-2026-000001").get("history")).hasSize(1);
    }

    /** Varios motivos se unen con "; ", en el orden de §6.1. */
    @Test
    void acumulaTodosLosMotivosDeBloqueoSeparadosPorPuntoYComa() throws Exception {
        resources.findById("EQ-001").orElseThrow()
                .setStatus(pe.vizcacha.laboratorio.domain.enums.EquipmentStatus.EN_MANTENIMIENTO);
        resources.findById("REA-001").orElseThrow().setQty(0);
        resources.findById("MAT-001").orElseThrow().setQty(0);

        post422("/api/samples/MUE-2026-000001/state", Map.of("state", "EN ANÁLISIS"),
                "No se puede iniciar el análisis: "
                        + "Analizador hematológico: equipo en mantenimiento; "
                        + "Reactivo hematológico: reactivo sin stock; "
                        + "Tubos EDTA: material insuficiente");
    }

    @Test
    void elFlujoEsEstrictamenteLineal() throws Exception {
        // RECIBIDA no puede saltar a EN VALIDACIÓN
        post422("/api/samples/MUE-2026-000001/state", Map.of("state", "EN VALIDACIÓN"),
                "Transición no válida: una muestra en RECIBIDA solo puede pasar a EN ANÁLISIS.");

        // Una publicada no admite mas transiciones
        post422("/api/samples/MUE-2026-000006/state", Map.of("state", "PUBLICADA"),
                "La muestra ya está publicada: no admite más transiciones.");
    }

    @Test
    void sinRolesPublicarSigueSiendoUnPasoExplicito() throws Exception {
        post422("/api/samples/MUE-2026-000001/state", Map.of("state", "PUBLICADA"),
                "Transición no válida: una muestra en RECIBIDA solo puede pasar a EN ANÁLISIS.");
    }

    // -------------------------------------------------------- resultados §6.4.4

    @Test
    void soloSePuedenIngresarResultadosDeUnaMuestraEnAnalisis() throws Exception {
        // MUE-2026-000001 esta RECIBIDA
        post422("/api/samples/MUE-2026-000001/results",
                Map.of("values", Map.of(), "notes", "", "submit", false),
                "Selecciona una muestra en análisis para ingresar resultados.");
    }

    @Test
    void elBorradorNoMueveElEstadoNiCuentaComoPublicado() throws Exception {
        // MUE-2026-000002 esta EN ANÁLISIS, formato glucosa
        postOk("/api/samples/MUE-2026-000002/results", Map.of(
                "values", Map.of("Glucosa", "92"),
                "notes", "Pendiente de repetir.",
                "submit", false));

        assertThat(estado("MUE-2026-000002")).isEqualTo("EN ANÁLISIS");
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.samples[1].results.Glucosa").value("92"))
                .andExpect(jsonPath("$.samples[1].history.length()").value(2))
                .andExpect(jsonPath("$.audit[0].action").value("Borrador de resultado guardado"));
    }

    @Test
    void guardarResultadosVariasVecesActualizaSinDuplicarNiCambiarResponsable() throws Exception {
        String responsable = buscar("samples", "MUE-2026-000002").get("responsible").asText();

        postOk("/api/samples/MUE-2026-000002/results", Map.of(
                "values", Map.of("Glucosa", "92"), "notes", "", "submit", false));
        em.flush();
        postOk("/api/samples/MUE-2026-000002/results", Map.of(
                "values", Map.of("Glucosa", "95"), "notes", "", "submit", false));
        em.flush();
        postOk("/api/samples/MUE-2026-000002/results", Map.of(
                "values", Map.of("Glucosa", "97"), "notes", "Revisado", "submit", true));
        em.flush();

        JsonNode s = buscar("samples", "MUE-2026-000002");
        assertThat(s.get("state").asText()).isEqualTo("EN VALIDACIÓN");
        assertThat(s.get("results").get("Glucosa").asText()).isEqualTo("97");
        assertThat(s.get("responsible").asText()).isEqualTo(responsable);
        assertThat(em.createQuery(
                "select count(r) from VicunaResult r where r.sample.id = 'MUE-2026-000002'",
                Long.class).getSingleResult()).isEqualTo(1L);
    }

    @Test
    void enviarAValidacionExigeTodosLosParametros() throws Exception {
        post422("/api/samples/MUE-2026-000005/results",
                Map.of("values", Map.of("Colesterol total", "180"), "notes", "", "submit", true),
                "Completa todos los parámetros antes de enviar a validación.");
    }

    @Test
    void rechazaValoresNumericosInvalidosONegativos() throws Exception {
        post422("/api/samples/MUE-2026-000002/results",
                Map.of("values", Map.of("Glucosa", "-5"), "notes", "", "submit", false),
                "Los parámetros numéricos deben contener valores válidos, no negativos.");

        post422("/api/samples/MUE-2026-000002/results",
                Map.of("values", Map.of("Glucosa", "abc"), "notes", "", "submit", false),
                "Los parámetros numéricos deben contener valores válidos, no negativos.");
    }

    /** Un valor fuera de rango se acepta y se guarda: la aplicacion senala la
     *  desviacion en la interfaz, no la rechaza ni diagnostica. */
    @Test
    void unValorFueraDeRangoSeGuardaSinRechazarse() throws Exception {
        postOk("/api/samples/MUE-2026-000002/results", Map.of(
                "values", Map.of("Glucosa", "250"), "notes", "", "submit", true));

        assertThat(estado("MUE-2026-000002")).isEqualTo("EN VALIDACIÓN");
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.samples[1].results.Glucosa").value("250"));
    }

    /** Las observaciones por parametro viajan con el sufijo _obs. */
    @Test
    void guardaLaObservacionDeUnParametroConSuSufijo() throws Exception {
        Map<String, String> valores = new LinkedHashMap<>();
        valores.put("Glucosa", "92");
        valores.put("Glucosa_obs", "Repetido");

        postOk("/api/samples/MUE-2026-000002/results",
                Map.of("values", valores, "notes", "", "submit", false));

        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.samples[1].results.Glucosa").value("92"))
                .andExpect(jsonPath("$.samples[1].results['Glucosa_obs']").value("Repetido"));
    }

    /**
     * Una observación sobre un parámetro FUERA DE RANGO tiene que llegar
     * hasta el informe impreso. Es el caso que mas importa: el valor
     * desviado es justo el que lleva anotacion.
     */
    @Test
    void laObservacionDeUnValorFueraDeRangoLlegaAlInforme() throws Exception {
        Map<String, String> valores = new LinkedHashMap<>();
        valores.put("Glucosa", "250");
        valores.put("Glucosa_obs", "Paciente sin ayuno confirmado");

        postOk("/api/samples/MUE-2026-000002/results",
                Map.of("values", valores, "notes", "Repetir en ayunas.", "submit", true));
        postOk("/api/samples/MUE-2026-000002/state", Map.of("state", "VALIDADO"));
        postOk("/api/samples/MUE-2026-000002/state", Map.of("state", "PUBLICADA"));

        JsonNode informe = leerInforme("MUE-2026-000002");
        JsonNode fila = informe.get("rows").get(0);

        assertThat(fila.get("name").asText()).isEqualTo("Glucosa");
        assertThat(fila.get("value").asText()).isEqualTo("250");
        assertThat(fila.get("outOfRange").asBoolean()).isTrue();
        // Lo que se perdia: la observacion del parametro desviado.
        assertThat(fila.get("observation").asText())
                .isEqualTo("Paciente sin ayuno confirmado");

        assertThat(informe.get("notes").asText()).isEqualTo("Repetir en ayunas.");
        assertThat(informe.get("outOfRange").get(0).asText()).isEqualTo("Glucosa");
    }

    /** Un parametro sin observacion sale con el guion largo, no vacio. */
    @Test
    void unParametroSinObservacionSaleConGuion() throws Exception {
        JsonNode informe = leerInforme("MUE-2026-000006");

        assertThat(informe.get("rows").get(0).get("observation").asText()).isEqualTo("—");
    }

    private JsonNode leerInforme(String sampleId) throws Exception {
        byte[] cuerpo = mvc.perform(get("/api/samples/" + sampleId + "/report"))
                .andReturn().getResponse().getContentAsByteArray();
        return json.readTree(new String(cuerpo, StandardCharsets.UTF_8));
    }

    // ------------------------------------------------------- acceso general

    @Test
    void lasAccionesSeRegistranComoRegistroLocalSinCuentas() throws Exception {
        mvc.perform(post("/api/samples/MUE-2026-000009/state")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"state\":\"PUBLICADA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users").doesNotExist())
                .andExpect(jsonPath("$.audit[0].user").value("Responsable del laboratorio"))
                .andExpect(jsonPath("$.audit[0].role").value("Acceso general"));

        mvc.perform(post("/api/session/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"USR-002\"}"))
                .andExpect(status().is4xxClientError());
    }
}
