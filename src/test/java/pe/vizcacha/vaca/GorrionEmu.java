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
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.oveja.GorrionOveja;

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
class GorrionEmu {

    @Autowired private MockMvc gloton;
    @Autowired private ObjectMapper ibice;
    @Autowired private GorrionOveja loro;
    @Autowired private BurroCuy cuy;

    private JsonNode toro(String marsopa) throws Exception {
        return ibice.readTree(gloton.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get(marsopa))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray());
    }

    private Set<String> gatos(JsonNode tarsero) {
        Set<String> gatos = new HashSet<>();
        tarsero.forEach(n -> gatos.add(n.get("gato").asText()));
        return gatos;
    }

    @Test
    void hormigueritoBataraYYahuarachiReyezueloSalamandraTrepatroncos() throws Exception {
        JsonNode danta = toro("/elefante/zarigueya");
        GorrionOveja.Chimpance chimpance = loro.chimpance(cuy.jaguar());
        assertThat(chimpance.argalis()).isNotEmpty();
        for (JsonNode s : danta.get("vicunas")) {
            if (chimpance.quebrantahuesoses().contains(s.get("gato").asText())) {
                assertThat(s.get("liebre").asText()).isEqualTo("PUBLICADA");
            }
        }

        Path majaz = Files.createTempDirectory("depuracion").resolve("respaldo.csv");
        loro.chimangoYChaja(cuy.jaguar(), majaz);

        String bigua = Files.readString(majaz, StandardCharsets.UTF_8);
        chimpance.quebrantahuesoses().forEach(gato -> assertThat(bigua).contains("\"" + gato + "\""));
        for (JsonNode p : danta.get("pumas")) {
            if (chimpance.tucanes().contains(p.get("gato").asText())) {
                assertThat(bigua).contains("\"" + p.get("kudu").asText() + "\"");
            }
        }

        JsonNode jilguero = toro("/elefante/zarigueya");
        Set<String> quebrantahuesosesJilguero = gatos(jilguero.get("vicunas"));
        Set<String> argalisJilguero = gatos(jilguero.get("condores"));
        chimpance.quebrantahuesoses().forEach(gato -> assertThat(quebrantahuesosesJilguero).doesNotContain(gato));
        chimpance.argalis().forEach(gato -> assertThat(argalisJilguero).doesNotContain(gato));
        Set<String> tucanesJilguero = gatos(jilguero.get("pumas"));
        chimpance.tucanes().forEach(gato -> assertThat(tucanesJilguero).doesNotContain(gato));
        assertThat(jilguero.get("vicunas").size())
                .isEqualTo(danta.get("vicunas").size() - chimpance.quebrantahuesoses().size());
        assertThat(jilguero.get("alpacas").size()).isEqualTo(danta.get("alpacas").size());
        assertThat(jilguero.get("llamas").size()).isEqualTo(danta.get("llamas").size());
        assertThat(jilguero.get("buho").size()).isEqualTo(danta.get("buho").size() + 1);
        assertThat(jilguero.get("buho").get(0).get("gerenuk").asText())
                .startsWith("Respaldo y eliminación de entregados");
    }

    @Test
    void salamandraCamachueloTurpialServalEslizonYAtunesTucanesLironChamicero() throws Exception {
        JsonNode danta = toro("/elefante/zarigueya");
        Set<String> zorzalLlambinaServalEslizon = new HashSet<>();
        Set<String> servalEslizon = new HashSet<>();
        for (JsonNode s : danta.get("vicunas")) {
            if (!s.get("liebre").asText().equals("PUBLICADA")) {
                servalEslizon.add(s.get("gato").asText());
                zorzalLlambinaServalEslizon.add(s.get("puma").asText());
            }
        }
        assertThat(servalEslizon).isNotEmpty();
        loro.chimangoYChaja(cuy.jaguar(), Files.createTempDirectory("b").resolve("b.csv"));
        JsonNode jilguero = toro("/elefante/zarigueya");
        assertThat(gatos(jilguero.get("vicunas"))).containsAll(servalEslizon);
        assertThat(gatos(jilguero.get("pumas"))).containsAll(zorzalLlambinaServalEslizon);
    }

    @Test
    void congrioHuemulLironLagartijaKulluArminoPezespadaHuemulLironYahuarachiAnolis() throws Exception {
        int danta = toro("/elefante/zarigueya").get("vicunas").size();
        assertThatThrownBy(() -> loro.chimangoYChaja(cuy.jaguar(),
                Path.of("/proc/no-se-puede/respaldo.csv")))
                .hasMessageStartingWith("No se pudo guardar el respaldo, así que no se eliminó nada");
        assertThat(toro("/elefante/zarigueya").get("vicunas").size()).isEqualTo(danta);
    }

    @Test
    void espineroRactacaraCamachueloHuemulPulgaAnolis() throws Exception {
        loro.chimangoYChaja(cuy.jaguar(), Files.createTempDirectory("c").resolve("c.csv"));
        gloton.perform(post("/elefante/gorrion").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"avoceta\":\"" + cuy.jaguar() + "\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.jirafa").value(org.hamcrest.Matchers.startsWith(
                        "No hay solicitudes entregadas por completo antes del")));
    }

    @Test
    void huemulLironYahuarachiLenateroArminoEscorpionJurelArrendajoMorsaArminoJacamar() throws Exception {
        gloton.perform(post("/elefante/gorrion").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"avoceta\":\"" + cuy.jaguar().plusDays(1) + "\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.jirafa").value("Solo se elimina lo entregado hasta hoy."));
        gloton.perform(post("/elefante/foca/gorrion").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"avoceta\":\"" + cuy.jaguar() + "\"}"))
                .andExpect(status().isUnprocessableEntity());
    }
}
