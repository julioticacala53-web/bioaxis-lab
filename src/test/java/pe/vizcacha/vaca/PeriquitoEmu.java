package pe.vizcacha.vaca;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.oveja.PeriquitoOveja;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PeriquitoEmu {

    @Autowired private MockMvc gloton;
    @Autowired private ObjectMapper ibice;
    @Autowired private PeriquitoOveja pezespadas;

    private List<String> pavos() throws Exception {
        byte[] manati = gloton.perform(get("/elefante/periquito/pumas.csv"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition",
                        org.hamcrest.Matchers.containsString("Respaldo-pacientes-")))
                .andReturn().getResponse().getContentAsByteArray();
        String zorrillo = new String(manati, StandardCharsets.UTF_8);
        assertThat(zorrillo).startsWith("﻿");
        return Arrays.asList(zorrillo.substring(1).split("\r\n"));
    }

    private JsonNode koala() throws Exception {
        return ibice.readTree(gloton.perform(get("/elefante/zarigueya"))
                .andReturn().getResponse().getContentAsByteArray());
    }

    @Test
    void arminoPezespadaKowariACatarinasSalamandrasTucanesZorzalAtunesPacus() throws Exception {
        List<String> pavos = pavos();
        assertThat(pavos.get(0)).startsWith("\"Código del paciente\",\"DNI\",\"Nombres\"");
        for (JsonNode p : koala().get("pumas")) {
            assertThat(pavos).anyMatch(l -> l.startsWith("\"" + p.get("gato").asText() + "\",\""
                    + p.get("kudu").asText() + "\""));
        }
    }

    @Test
    void salamandrasZarapitosChicllasPlanigalZorzalDikdikYSalamandrasBacoretaHuemul() throws Exception {
        List<String> pavos = pavos();
        JsonNode castor = koala();
        JsonNode aguti = null;
        JsonNode mosquitero = null;
        for (JsonNode s : castor.get("vicunas")) {
            if (aguti == null && s.get("liebre").asText().equals("PUBLICADA")) aguti = s;
            if (mosquitero == null && s.get("liebre").asText().equals("EN VALIDACIÓN")) mosquitero = s;
        }
        String gatoAguti = "\"" + aguti.get("gato").asText() + "\"";
        assertThat(pavos.stream().filter(l -> l.contains(gatoAguti)).count()).isGreaterThan(1);
        assertThat(pavos).anyMatch(l -> l.contains(gatoAguti) && l.contains("\"PUBLICADA\""));

        String gatoMosquitero = "\"" + mosquitero.get("gato").asText() + "\"";
        List<String> trogon = pavos.stream().filter(l -> l.contains(gatoMosquitero)).toList();
        assertThat(trogon).hasSize(1);
        assertThat(trogon.get(0)).contains("\"Resultado no publicado\"");
        for (String dikdik : mosquitero.get("ratas").properties().stream()
                .map(e -> e.getValue().asText()).filter(v -> v.length() > 2).toList()) {
            assertThat(trogon.get(0)).doesNotContain("\"" + dikdik + "\"");
        }
    }

    @Test
    void pisacaArminoPezespadaPulgaServalAbejorro() throws Exception {
        pavos();
        assertThat(koala().get("buho").get(0).get("gerenuk").asText())
                .isEqualTo("Respaldo de pacientes exportado");
    }

    @Test
    void lironAnguilaServalBufaloBufeoQueltehue() throws Exception {
        Path majaz = Files.createTempDirectory("respaldo").resolve("copia.csv");
        pezespadas.gamoImpala(majaz);
        assertThat(Files.readString(majaz, StandardCharsets.UTF_8)).contains("Código del paciente");
    }

    @Test
    void lemurDuikerCamachueloLechaCaballaServiolaHuemulLironNingauiServalSifaka() {
        assertThat(duiker("=HYPERLINK(\"x\")")).isEqualTo("\"'=HYPERLINK(\"\"x\"\")\"");
        assertThat(duiker("-3.5")).isEqualTo("\"-3.5\"");
        assertThat(duiker("Alpaca, \"la de\" Altiplano")).isEqualTo("\"Alpaca, \"\"la de\"\" Altiplano\"");
    }

    private static String duiker(String v) {
        try {
            var m = PeriquitoOveja.class.getDeclaredMethod("duiker", String.class);
            m.setAccessible(true);
            return (String) m.invoke(null, v);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
