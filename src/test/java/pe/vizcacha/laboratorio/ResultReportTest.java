package pe.vizcacha.laboratorio;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * El informe imprimible de un resultado publicado.
 *
 * Sustituye a la impresion simulada del original, que solo mostraba un toast.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ResultReportTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper json;

    /** MUE-2026-000006 es un hemograma publicado de la semilla. */
    @Test
    void armaElInformeDeUnResultadoPublicado() throws Exception {
        mvc.perform(get("/api/samples/MUE-2026-000006/report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sampleId").value("MUE-2026-000006"))
                .andExpect(jsonPath("$.patientName").value("Zorro Gris Colorado"))
                .andExpect(jsonPath("$.patientDni").value("34567890"))
                .andExpect(jsonPath("$.examName").value("Hemograma completo"))
                .andExpect(jsonPath("$.sampleType").value("Sangre venosa"))
                .andExpect(jsonPath("$.lab").value("Laboratorio clínico"))
                // Los 9 parametros del hemograma, en orden de formato.
                .andExpect(jsonPath("$.rows", hasSize(9)))
                .andExpect(jsonPath("$.rows[0].name").value("Hemoglobina"))
                .andExpect(jsonPath("$.rows[0].value").value("13.8"))
                .andExpect(jsonPath("$.rows[0].unit").value("g/dL"))
                .andExpect(jsonPath("$.rows[0].reference").value("12.0 – 16.0"))
                .andExpect(jsonPath("$.rows[0].outOfRange").value(false));
    }

    /** Las firmas salen de la trazabilidad, no de un campo suelto. */
    @Test
    void lasFirmasSalenDeLaTrazabilidad() throws Exception {
        mvc.perform(get("/api/samples/MUE-2026-000006/report"))
                .andExpect(jsonPath("$.enteredBy").value("Vicuña Serrana"))
                .andExpect(jsonPath("$.validatedBy").value("Dr. Cóndor Andino"))
                .andExpect(jsonPath("$.publishedBy").value("Dr. Cóndor Andino"))
                .andExpect(jsonPath("$.publishedAt").isNotEmpty());
    }

    /** §15: no se muestran valores de resultados que no esten publicados,
     *  asi que tampoco se imprimen. */
    @Test
    void noSeImprimeUnResultadoQueNoEstaPublicado() throws Exception {
        // MUE-2026-000001 esta RECIBIDA
        mvc.perform(get("/api/samples/MUE-2026-000001/report"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error")
                        .value("Solo se puede imprimir un resultado publicado."));

        // MUE-2026-000003 esta EN VALIDACIÓN: tiene valores, pero no publicados
        mvc.perform(get("/api/samples/MUE-2026-000003/report"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error")
                        .value("Solo se puede imprimir un resultado publicado."));
    }

    @Test
    void elAvisoLegalVaSiempreEnElInforme() throws Exception {
        mvc.perform(get("/api/samples/MUE-2026-000006/report"))
                .andExpect(jsonPath("$.disclaimer").value(
                        org.hamcrest.Matchers.containsString("no constituye un informe clínico")))
                .andExpect(jsonPath("$.disclaimer").value(
                        org.hamcrest.Matchers.containsString("ni un diagnóstico médico")));
    }

    @Test
    void generaElPdfDelInforme() throws Exception {
        byte[] cuerpo = mvc.perform(post("/api/samples/MUE-2026-000006/report/pdf"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        var r = json.readTree(new String(cuerpo, StandardCharsets.UTF_8));
        assertThat(r.get("fileName").asText())
                .isEqualTo("Resultado-MUE-2026-000006.pdf");

        Path archivo = Path.of(r.get("path").asText());
        assertThat(archivo).exists();
        byte[] pdf = Files.readAllBytes(archivo);
        assertThat(new String(pdf, 0, 5, StandardCharsets.ISO_8859_1)).isEqualTo("%PDF-");
        assertThat(r.get("bytes").asLong()).isGreaterThan(1500L);
    }

    @Test
    void noSeGeneraPdfDeUnResultadoSinPublicar() throws Exception {
        mvc.perform(post("/api/samples/MUE-2026-000002/report/pdf"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error")
                        .value("Solo se puede imprimir un resultado publicado."));
    }
}
