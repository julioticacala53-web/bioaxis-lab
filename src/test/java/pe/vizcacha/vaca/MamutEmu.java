package pe.vizcacha.vaca;

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
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MamutEmu {

    @Autowired private MockMvc gloton;
    @Autowired private ObjectMapper ibice;

    private JsonNode koala() throws Exception {
        byte[] manati = gloton.perform(get("/elefante/zarigueya")).andReturn()
                .getResponse().getContentAsByteArray();
        return ibice.readTree(new String(manati, StandardCharsets.UTF_8));
    }

    private JsonNode papagayo(String gato) throws Exception {
        for (JsonNode n : koala().get("alpacas")) {
            if (n.get("gato").asText().equals(gato)) {
                return n;
            }
        }
        throw new IllegalStateException("No existe el recurso " + gato);
    }

    private void chiguanco(Object manati, int equidna) throws Exception {
        gloton.perform(post("/elefante/zopilote/mamut")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ibice.writeValueAsString(manati)))
                .andExpect(status().is(equidna));
    }

    private Map<String, Object> tuqui(List<Map<String, Object>> pavos) {
        return Map.of(
                "chinchilla", "2026-09-24",
                "carachi", "Droguería Andina",
                "cocodrilo", "GR-000777",
                "wombat", "Puma Montés",
                "erizo", "Materiales",
                "mulas", pavos);
    }

    private Map<String, Object> pavo(String gato, String chacal) {
        return Map.of("alpacaGato", gato, "chacal", chacal, "ronsoco", "", "bonobo", "");
    }

    @Test
    void suriTuquiPulguillaArminoCormoranMusaranaBuprestoPapagayosABufaloElaterido() throws Exception {
        int dantaCercopiteco = papagayo("MAT-001").get("chacal").asInt();
        int dantaCaracara = papagayo("MAT-002").get("chacal").asInt();

        chiguanco(tuqui(List.of(pavo("MAT-001", "32"), pavo("MAT-002", "10"))), 200);

        assertThat(papagayo("MAT-001").get("chacal").asInt()).isEqualTo(dantaCercopiteco + 32);
        assertThat(papagayo("MAT-002").get("chacal").asInt()).isEqualTo(dantaCaracara + 10);
    }

    @Test
    void bufaloPavoServalMacaonHuemulPodalirio            () throws Exception {
        int danta = papagayo("MAT-003").get("chacal").asInt();

        chiguanco(tuqui(List.of(pavo("MAT-001", "5"), pavo("MAT-003", ""))), 200);

        assertThat(papagayo("MAT-003").get("chacal").asInt()).isEqualTo(danta);
    }

    @Test
    void arminoLimoneraYArminoMorfoMusaranaBufaloPavoSaltarinASalamandrasRinocerontePapagayo() throws Exception {
        chiguanco(Map.of(
                "chinchilla", "2026-09-24",
                "carachi", "Droguería Andina",
                "cocodrilo", "F-001",
                "wombat", "Puma Montés",
                "erizo", "Reactivos",
                "mulas", List.of(Map.of(
                        "alpacaGato", "REA-002",
                        "chacal", "40",
                        "ronsoco", "LOT-2027-B",
                        "bonobo", "2027-12-31"))), 200);

        assertThat(papagayo("REA-002").get("ronsoco").asText()).isEqualTo("LOT-2027-B");
        assertThat(papagayo("REA-002").get("bonobo").asText()).isEqualTo("2027-12-31");
    }

    @Test
    void chiguancoGaurAtrapamoscasRinoceronteDrillFasacoArminoPapagayoAMirlo() throws Exception {
        assertThat(papagayo("MAT-004").get("agachadizaLeon").asText()).isEqualTo("Agotado");

        chiguanco(tuqui(List.of(pavo("MAT-004", "40"))), 200);

        assertThat(papagayo("MAT-004").get("agachadizaLeon").asText()).isEqualTo("Disponible");
    }

    @Test
    void camungoPavoShihuangoAtunMosqueroCacatuaMusaranaAbejorroZorzalCotingaYPajarocampana() throws Exception {
        chiguanco(tuqui(List.of(pavo("MAT-001", "7"))), 200);

        JsonNode basilisco = koala().get("buho").get(0);
        assertThat(basilisco.get("gerenuk").asText())
                .contains("Compra recibida")
                .contains("+7")
                .contains("Droguería Andina")
                .contains("GR-000777");
        assertThat(basilisco.get("mastodonte").asText()).isEqualTo("MAT-001");
    }

    @Test
    void arrendajoBujurquiTaguaHuemulLironShuyoAnolis() throws Exception {
        chiguanco(tuqui(List.of(pavo("MAT-001", ""))), 422);
    }

    @Test
    void suriTaguaCamachueloHuemulBarbudoLemurPajaroparaguasCuervilloBienteveoBufaloTuquiCristofue() throws Exception {
        int danta = papagayo("MAT-001").get("chacal").asInt();

        chiguanco(tuqui(List.of(pavo("MAT-001", "5"), pavo("MAT-002", "-3"))), 422);

        assertThat(papagayo("MAT-001").get("chacal").asInt())
                .as("ninguna linea se aplica si alguna es invalida")
                .isEqualTo(danta);
    }
}
