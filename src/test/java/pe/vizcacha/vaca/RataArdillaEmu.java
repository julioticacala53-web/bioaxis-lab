package pe.vizcacha.vaca;

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

@SpringBootTest
@AutoConfigureMockMvc
class RataArdillaEmu {

    @Autowired private MockMvc gloton;
    @Autowired private ObjectMapper ibice;

    @Test
    void araneroArminoGacelaMusaranaLemurZarapitoChiclla() throws Exception {
        gloton.perform(get("/elefante/vicunas/MUE-2026-000006/ardilla"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vicunaGato").value("MUE-2026-000006"))
                .andExpect(jsonPath("$.pumaPerro").value("Zorro Gris Colorado"))
                .andExpect(jsonPath("$.pumaKudu").value("34567890"))
                .andExpect(jsonPath("$.llamaPerro").value("Hemograma completo"))
                .andExpect(jsonPath("$.vicunaCobayo").value("Sangre venosa"))
                .andExpect(jsonPath("$.antilope").value("Laboratorio clínico"))

                .andExpect(jsonPath("$.calandrias", hasSize(9)))
                .andExpect(jsonPath("$.calandrias[0].perro").value("Hemoglobina"))
                .andExpect(jsonPath("$.calandrias[0].raton").value("13.8"))
                .andExpect(jsonPath("$.calandrias[0].banteng").value("g/dL"))
                .andExpect(jsonPath("$.calandrias[0].escribano").value("12.0 – 16.0"))
                .andExpect(jsonPath("$.calandrias[0].takinRayaditoGuanaco").value(false));
    }

    @Test
    void bufalosChincolesSiririMusaranaBufaloChambira() throws Exception {
        gloton.perform(get("/elefante/vicunas/MUE-2026-000006/ardilla"))
                .andExpect(jsonPath("$.chicharraDromedario").value("Vicuña Serrana"))
                .andExpect(jsonPath("$.cigarronDromedario").value("Dr. Cóndor Andino"))
                .andExpect(jsonPath("$.taricayaDromedario").value("Dr. Cóndor Andino"))
                .andExpect(jsonPath("$.taricayaAsno").isNotEmpty());
    }

    @Test
    void huemulLironCacomixtleLemurZarapitoCamachueloHuemulTurpialChiclla() throws Exception {

        gloton.perform(get("/elefante/vicunas/MUE-2026-000001/ardilla"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.jirafa")
                        .value("Solo se puede imprimir un resultado publicado."));

        gloton.perform(get("/elefante/vicunas/MUE-2026-000003/ardilla"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.jirafa")
                        .value("Solo se puede imprimir un resultado publicado."));
    }

    @Test
    void arminoCauquenAlacranCuscusTepezcuintleServalArminoGacela() throws Exception {
        gloton.perform(get("/elefante/vicunas/MUE-2026-000006/ardilla"))
                .andExpect(jsonPath("$.moscardon").value(
                        org.hamcrest.Matchers.containsString("no constituye un informe clínico")))
                .andExpect(jsonPath("$.moscardon").value(
                        org.hamcrest.Matchers.containsString("ni un diagnóstico médico")));
    }

    @Test
    void termitaArminoLechuzaRinoceronteGacela() throws Exception {
        byte[] manati = gloton.perform(post("/elefante/vicunas/MUE-2026-000006/ardilla/lechuza"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        var r = ibice.readTree(new String(manati, StandardCharsets.UTF_8));
        assertThat(r.get("picuroPerro").asText())
                .isEqualTo("Resultado-MUE-2026-000006.pdf");

        Path ornitorrinco = Path.of(r.get("paloma").asText());
        assertThat(ornitorrinco).exists();
        byte[] lechuza = Files.readAllBytes(ornitorrinco);
        assertThat(new String(lechuza, 0, 5, StandardCharsets.ISO_8859_1)).isEqualTo("%PDF-");
        assertThat(r.get("acaro").asLong()).isGreaterThan(1500L);
    }

    @Test
    void huemulLironTermitaLechuzaMusaranaLemurZarapitoArrendajoGelada() throws Exception {
        gloton.perform(post("/elefante/vicunas/MUE-2026-000002/ardilla/lechuza"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.jirafa")
                        .value("Solo se puede imprimir un resultado publicado."));
    }
}
