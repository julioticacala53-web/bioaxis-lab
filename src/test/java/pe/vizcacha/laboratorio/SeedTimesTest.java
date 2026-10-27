package pe.vizcacha.laboratorio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import pe.vizcacha.laboratorio.config.DemoClock;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Las horas de la semilla tienen que ser creibles.
 *
 * Existe por un fallo real: el original clavaba las muestras del dia entre
 * las 08:10 y las 11:40 porque vivia congelado a esa hora. Con el reloj del
 * equipo, abrir la aplicacion a las 08:48 mostraba muestras tomadas a las
 * 11:40 y trazas firmadas a las 15:15. El dashboard dejaba de ser creible.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SeedTimesTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper json;
    @Autowired private DemoClock clock;

    private JsonNode snapshot() throws Exception {
        byte[] cuerpo = mvc.perform(get("/api/database")).andReturn()
                .getResponse().getContentAsByteArray();
        return json.readTree(new String(cuerpo, StandardCharsets.UTF_8));
    }

    @Test
    void ningunaMuestraDeLaSemillaEstaEnElFuturo() throws Exception {
        LocalDateTime ahora = clock.timestamp();

        for (JsonNode s : snapshot().get("samples")) {
            LocalDateTime tomada = LocalDateTime.parse(s.get("date").asText());
            assertThat(tomada)
                    .as("la muestra %s está tomada en el futuro", s.get("id").asText())
                    .isBeforeOrEqualTo(ahora);
        }
    }

    @Test
    void ningunPasoDeTrazabilidadEstaEnElFuturo() throws Exception {
        LocalDateTime ahora = clock.timestamp();

        for (JsonNode s : snapshot().get("samples")) {
            for (JsonNode paso : s.get("history")) {
                LocalDateTime cuando = LocalDateTime.parse(paso.get("date").asText());
                assertThat(cuando)
                        .as("el paso %s de %s está en el futuro",
                                paso.get("state").asText(), s.get("id").asText())
                        .isBeforeOrEqualTo(ahora);
            }
        }
    }

    @Test
    void ningunaEntradaDeAuditoriaEstaEnElFuturo() throws Exception {
        LocalDateTime ahora = clock.timestamp();

        for (JsonNode a : snapshot().get("audit")) {
            assertThat(LocalDateTime.parse(a.get("date").asText()))
                    .as("la auditoría %s está en el futuro", a.get("id").asText())
                    .isBeforeOrEqualTo(ahora);
        }
    }

    /** Las 12 del dia siguen siendo de HOY, repartidas y en orden. */
    @Test
    void lasMuestrasDelDiaSonDeHoyYVanEnOrden() throws Exception {
        String hoy = clock.todayIso();
        LocalDateTime anterior = null;
        int delDia = 0;

        for (JsonNode s : snapshot().get("samples")) {
            String id = s.get("id").asText();
            if (id.compareTo("MUE-2026-000013") >= 0) {
                continue;                       // las historicas son de dias previos
            }
            delDia++;
            assertThat(s.get("date").asText()).startsWith(hoy);

            LocalDateTime tomada = LocalDateTime.parse(s.get("date").asText());
            if (anterior != null) {
                assertThat(tomada).isAfterOrEqualTo(anterior);
            }
            anterior = tomada;
        }
        assertThat(delDia).isEqualTo(12);
    }

    /** Y las historicas siguen en sus seis dias anteriores. */
    @Test
    void lasHistoricasSiguenEnLosSeisDiasAnteriores() throws Exception {
        for (JsonNode s : snapshot().get("samples")) {
            if (s.get("id").asText().compareTo("MUE-2026-000013") < 0) {
                continue;
            }
            java.time.LocalDate dia = LocalDateTime
                    .parse(s.get("date").asText()).toLocalDate();
            assertThat(dia).isBefore(clock.today());
            assertThat(dia).isAfterOrEqualTo(clock.today().minusDays(6));
        }
    }
}
