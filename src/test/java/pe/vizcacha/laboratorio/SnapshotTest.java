package pe.vizcacha.laboratorio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Entregable de la Fase 2: GET /api/database devuelve la semilla completa con
 * la forma exacta de §5.2.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SnapshotTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void devuelveLasCifrasDeLaSemilla() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patients", hasSize(5)))
                .andExpect(jsonPath("$.exams", hasSize(8)))
                .andExpect(jsonPath("$.resources", hasSize(18)))
                .andExpect(jsonPath("$.requests", hasSize(35)))
                .andExpect(jsonPath("$.samples", hasSize(42)))
                .andExpect(jsonPath("$.maintenance", hasSize(1)))
                .andExpect(jsonPath("$.audit", hasSize(3)))
                .andExpect(jsonPath("$.users").doesNotExist())
                .andExpect(jsonPath("$.appearance").value("gris"))
                .andExpect(jsonPath("$.lab").value("Laboratorio clínico"))
                .andExpect(jsonPath("$.branch").value("Laboratorio Central"))
                .andExpect(jsonPath("$.notifications").value(true));
    }

    /**
     * Los nombres son literales del original: min y no minQty, time y no
     * timeLabel, sample y no sampleType, last/next y no lastMaintenance.
     */
    @Test
    void usaLosNombresDeCampoLiteralesDelContrato() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.patients[0].first").value("Alpaca"))
                .andExpect(jsonPath("$.patients[0].last").value("Blanca Suri"))
                .andExpect(jsonPath("$.patients[0].firstName").doesNotExist())
                .andExpect(jsonPath("$.exams[0].time").value("24 horas"))
                .andExpect(jsonPath("$.exams[0].sample").value("Sangre venosa"))
                .andExpect(jsonPath("$.exams[0].timeLabel").doesNotExist())
                .andExpect(jsonPath("$.exams[0].sampleType").doesNotExist())
                .andExpect(jsonPath("$.resources[5].min").value(20))
                .andExpect(jsonPath("$.resources[5].minQty").doesNotExist())
                // Relativas al dia de hoy, que en los tests esta fijado en
                // 2026-09-24: ultimo mantenimiento hace 3 meses, proximo
                // dentro de 3.
                .andExpect(jsonPath("$.resources[0].last").value("2026-06-24"))
                .andExpect(jsonPath("$.resources[0].next").value("2026-12-24"))
                .andExpect(jsonPath("$.audit[0].record").value("MUE-2026-000003"))
                .andExpect(jsonPath("$.audit[0].user").value("Vicuña Serrana"));
    }

    /** Equipos, reactivos, materiales: no es el orden alfabetico de los ids. */
    @Test
    void ordenaLosRecursosPorFamiliaComoElOriginal() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.resources[0].id").value("EQ-001"))
                .andExpect(jsonPath("$.resources[4].id").value("EQ-005"))
                .andExpect(jsonPath("$.resources[5].id").value("REA-001"))
                .andExpect(jsonPath("$.resources[10].id").value("REA-006"))
                .andExpect(jsonPath("$.resources[11].id").value("MAT-001"))
                .andExpect(jsonPath("$.resources[17].id").value("MAT-007"));
    }

    /**
     * El original no ordena la auditoria por fecha: la semilla queda en orden
     * ascendente y lo nuevo entra por delante con unshift.
     */
    @Test
    void mantieneElOrdenDeArrayDeLaAuditoria() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.audit[0].id").value("AUD-000001"))
                .andExpect(jsonPath("$.audit[1].id").value("AUD-000002"))
                .andExpect(jsonPath("$.audit[2].id").value("AUD-000003"));
    }

    @Test
    void reparteLasMuestrasPorEstadoComoElDashboard() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.samples[?(@.state == 'RECIBIDA')]", hasSize(5)))
                .andExpect(jsonPath("$.samples[?(@.state == 'EN ANÁLISIS')]", hasSize(2)))
                .andExpect(jsonPath("$.samples[?(@.state == 'EN VALIDACIÓN')]", hasSize(2)))
                .andExpect(jsonPath("$.samples[?(@.state == 'VALIDADO')]", hasSize(1)))
                .andExpect(jsonPath("$.samples[?(@.state == 'PUBLICADA')]", hasSize(32)));
    }

    /**
     * Los valores precargados son min + (max - min) * 0.45 formateados como
     * en JavaScript: "13.8" y no "13.80", "262.5" y no "262.50".
     */
    @Test
    void precargaLosValoresDelHemogramaComoEnLaCaptura50() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.samples[2].id").value("MUE-2026-000003"))
                .andExpect(jsonPath("$.samples[2].state").value("EN VALIDACIÓN"))
                .andExpect(jsonPath("$.samples[2].results.Hemoglobina").value("13.8"))
                .andExpect(jsonPath("$.samples[2].results.Hematocrito").value("40.5"))
                .andExpect(jsonPath("$.samples[2].results.Leucocitos").value("6.7"))
                .andExpect(jsonPath("$.samples[2].results.Plaquetas").value("262.5"))
                .andExpect(jsonPath("$.samples[2].results.Neutrófilos").value("53.5"))
                .andExpect(jsonPath("$.samples[2].results.Linfocitos").value("31.25"))
                .andExpect(jsonPath("$.samples[2].results.Monocitos").value("5.6"))
                .andExpect(jsonPath("$.samples[2].results.Eosinófilos").value("2.7"))
                .andExpect(jsonPath("$.samples[2].results.Basófilos").value("0.9"));
    }

    /** Una muestra sin procesar no trae valores: no se inventan resultados. */
    @Test
    void lasMuestrasRecibidasNoTraenValores() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.samples[0].id").value("MUE-2026-000001"))
                .andExpect(jsonPath("$.samples[0].state").value("RECIBIDA"))
                .andExpect(jsonPath("$.samples[0].results.length()").value(0))
                .andExpect(jsonPath("$.samples[0].history", hasSize(1)));
    }

    /** La trazabilidad se recorta hasta el estado actual y la firma el medico
     *  desde el paso VALIDADO. */
    @Test
    void recortaLaTrazabilidadHastaElEstadoActual() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.samples[8].id").value("MUE-2026-000009"))
                .andExpect(jsonPath("$.samples[8].state").value("VALIDADO"))
                .andExpect(jsonPath("$.samples[8].history", hasSize(4)))
                .andExpect(jsonPath("$.samples[8].history[0].state").value("RECIBIDA"))
                .andExpect(jsonPath("$.samples[8].history[0].user").value("Vicuña Serrana"))
                .andExpect(jsonPath("$.samples[8].history[3].state").value("VALIDADO"))
                .andExpect(jsonPath("$.samples[8].history[3].user").value("Dr. Cóndor Andino"));
    }

    @Test
    void laSolicitudDeCondorLlevaTresExamenes() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.requests[1].id").value("SOL-000002"))
                .andExpect(jsonPath("$.requests[1].exams",
                        containsInAnyOrder("EX-001", "EX-002", "EX-003")))
                .andExpect(jsonPath("$.requests[3].exams",
                        containsInAnyOrder("EX-001", "EX-002", "EX-004")));
    }

    /** El mantenimiento sin finalizar manda cadena vacia, no null. */
    @Test
    void elMantenimientoEnProcesoNoTieneFechaReal() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.maintenance[0].id").value("MAN-000001"))
                .andExpect(jsonPath("$.maintenance[0].state").value("En proceso"))
                .andExpect(jsonPath("$.maintenance[0].actual").value(""))
                .andExpect(jsonPath("$.maintenance[0].equipment").value("EQ-003"));
    }

    /** EQ-003 arranca bloqueado y MAT-004 en cero: es lo que deja 4 de 8
     *  examenes disponibles cuando se calcule en la Fase 3. */
    @Test
    void laSemillaDejaElLaboratorioEnElEstadoEsperado() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.resources[2].id").value("EQ-003"))
                .andExpect(jsonPath("$.resources[2].status").value("EN MANTENIMIENTO"))
                .andExpect(jsonPath("$.resources[6].id").value("REA-002"))
                .andExpect(jsonPath("$.resources[6].qty").value(12))
                .andExpect(jsonPath("$.resources[6].min").value(20))
                .andExpect(jsonPath("$.resources[6].expires").value("2026-10-08"))
                .andExpect(jsonPath("$.resources[14].id").value("MAT-004"))
                .andExpect(jsonPath("$.resources[14].qty").value(0));
    }
}
