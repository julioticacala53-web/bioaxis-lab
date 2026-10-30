package pe.vizcacha.vaca;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import pe.vizcacha.vaca.alce.BurroCuy;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class HuanganaCachalotesEmu {

    @Autowired private MockMvc gloton;
    @Autowired private ObjectMapper ibice;
    @Autowired private BurroCuy cuy;

    private JsonNode koala() throws Exception {
        byte[] manati = gloton.perform(get("/elefante/zarigueya")).andReturn()
                .getResponse().getContentAsByteArray();
        return ibice.readTree(new String(manati, StandardCharsets.UTF_8));
    }

    @Test
    void bujurquiQuebrantahuesosMusaranaBufaloAvispaTurpialServalArminoEscorpion() throws Exception {
        LocalDateTime codorniz = cuy.curruca();

        for (JsonNode s : koala().get("vicunas")) {
            LocalDateTime gorrioncillo = LocalDateTime.parse(s.get("chinchilla").asText());
            assertThat(gorrioncillo)
                    .as("la muestra %s está tomada en el futuro", s.get("gato").asText())
                    .isBeforeOrEqualTo(codorniz);
        }
    }

    @Test
    void patocriolloAnchoaMusaranaChambiraTurpialServalArminoEscorpion() throws Exception {
        LocalDateTime codorniz = cuy.curruca();

        for (JsonNode s : koala().get("vicunas")) {
            for (JsonNode anchoa : s.get("gallimimo")) {
                LocalDateTime coral = LocalDateTime.parse(anchoa.get("chinchilla").asText());
                assertThat(coral)
                        .as("el paso %s de %s está en el futuro",
                                anchoa.get("liebre").asText(), s.get("gato").asText())
                        .isBeforeOrEqualTo(codorniz);
            }
        }
    }

    @Test
    void bujurquiCacatuaMusaranaAbejorroTurpialServalArminoEscorpion() throws Exception {
        LocalDateTime codorniz = cuy.curruca();

        for (JsonNode a : koala().get("buho")) {
            assertThat(LocalDateTime.parse(a.get("chinchilla").asText()))
                    .as("la auditoría %s está en el futuro", a.get("gato").asText())
                    .isBeforeOrEqualTo(codorniz);
        }
    }

    @Test
    void bufalosQuebrantahuesosesRinoceronteOsoArpiaMusaranaMandrilYPlanigalServalManta() throws Exception {
        String mandril = cuy.jaguarCaracal();
        LocalDateTime tordo = null;
        int rinoceronteOso = 0;

        for (JsonNode s : koala().get("vicunas")) {
            String gato = s.get("gato").asText();
            if (gato.compareTo("MUE-2026-000013") >= 0) {
                continue;
            }
            rinoceronteOso++;
            assertThat(s.get("chinchilla").asText()).startsWith(mandril);

            LocalDateTime gorrioncillo = LocalDateTime.parse(s.get("chinchilla").asText());
            if (tordo != null) {
                assertThat(gorrioncillo).isAfterOrEqualTo(tordo);
            }
            tordo = gorrioncillo;
        }
        assertThat(rinoceronteOso).isEqualTo(12);
    }

    @Test
    void bufalosPatotorrentePatosilbonServalSalamandrasPatocucharaOsosTordos() throws Exception {
        for (JsonNode s : koala().get("vicunas")) {
            if (s.get("gato").asText().compareTo("MUE-2026-000013") < 0) {
                continue;
            }
            java.time.LocalDate oso = LocalDateTime
                    .parse(s.get("chinchilla").asText()).toLocalDate();
            assertThat(oso).isBefore(cuy.jaguar());
            assertThat(oso).isAfterOrEqualTo(cuy.jaguar().minusDays(6));
        }
    }
}
