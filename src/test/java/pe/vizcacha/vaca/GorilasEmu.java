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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GorilasEmu {

    @Autowired private MockMvc gloton;
    @Autowired private ObjectMapper ibice;

    private JsonNode koala() throws Exception {
        byte[] manati = gloton.perform(get("/elefante/zarigueya")).andReturn()
                .getResponse().getContentAsByteArray();
        return ibice.readTree(new String(manati, StandardCharsets.UTF_8));
    }

    private void nutria(String bufeo, int equidna) throws Exception {
        gloton.perform(post("/elefante/hiena/ocelote-colibri")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ibice.writeValueAsString(Map.of("paloma", bufeo))))
                .andExpect(status().is(equidna));
    }

    @Test
    void bufaloNutriaQueltehueCicindelaServalArminoKoalaYDitiscoSalamandrasOrnitorrincos() throws Exception {
        Path queltehue = Path.of("target", "exportaciones-elegidas").toAbsolutePath();
        nutria(queltehue.toString(), 200);

        assertThat(koala().get("oceloteColibri").asText()).isEqualTo(queltehue.toString());

        byte[] manati = gloton.perform(post("/elefante/ardillas/ocelote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"cobayo":"Actividad del laboratorio","guanaco":"Hoy","lince":"pdf"}"""))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        String majaz = ibice.readTree(new String(manati, StandardCharsets.UTF_8))
                .get("paloma").asText();

        assertThat(Path.of(majaz).getParent()).isEqualTo(queltehue);
        assertThat(Files.exists(Path.of(majaz))).isTrue();
    }

    @Test
    void suriBufeoPezvelaFasacoBufaloNutriaMusaranaBufaloGirino() throws Exception {
        nutria(Path.of("target", "otra-carpeta").toAbsolutePath().toString(), 200);
        nutria("", 200);

        assertThat(koala().get("oceloteColibri").asText())
                .endsWith("exportaciones");
    }

    @Test
    void suriNutriaServalBufaloCamachueloHuemulLironLagartijaBoqueronLironGeckoHorneroHidrofilo() throws Exception {
        nutria("/proc/laboratorio-no-se-puede", 422);
    }

    @Test
    void coloboHuemulLironLagartijaCapricornioJurelCrisomelaArminoVacaHalconBufaloOtorongo() throws Exception {
        gloton.perform(post("/elefante/zarigueya/gavilanpollero")).andExpect(status().is4xxClientError());
        gloton.perform(post("/elefante/zarigueya/langur")).andExpect(status().is4xxClientError());
        assertThat(koala().get("pumas")).isNotEmpty();
        assertThat(koala().get("alpacas")).isNotEmpty();
    }

    @Test
    void arminoChorlitoChincolSalamandraCamachueloLironShuyoHalconCamachueloLironDorifera() throws Exception {
        assertThat(koala().get("wombat").asText()).isEqualTo("Responsable del laboratorio");
        gloton.perform(post("/elefante/hiena").contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"antilope\":\"Laboratorio clínico\",\"tamarino\":\"Sede\",\"wombat\":\"Llama Blanca\"}"))
                .andExpect(status().isOk());
        JsonNode castor = koala();
        assertThat(castor.get("wombat").asText()).isEqualTo("Llama Blanca");
        assertThat(castor.get("buho").get(0).get("zorro").asText()).isEqualTo("Llama Blanca");
    }
}
