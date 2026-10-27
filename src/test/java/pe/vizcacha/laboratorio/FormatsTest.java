package pe.vizcacha.laboratorio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FormatsTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void unParametroNumericoNoLlevaListaDeOpciones() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formats.lipid[0].name").value("Colesterol total"))
                .andExpect(jsonPath("$.formats.lipid[0].min").value(0.0))
                .andExpect(jsonPath("$.formats.lipid[0].max").value(200.0))
                .andExpect(jsonPath("$.formats.lipid[0].options").doesNotExist());
    }

    @Test
    void unMinimoDeCeroSeMantiene() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.formats.hemogram[7].name").value("Eosinófilos"))
                .andExpect(jsonPath("$.formats.hemogram[7].min").value(0.0))
                .andExpect(jsonPath("$.formats.lipid[2].name").value("LDL"))
                .andExpect(jsonPath("$.formats.lipid[2].min").value(0.0));
    }

    @Test
    void unParametroDeOpcionesNoLlevaRango() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.formats.urine[0].name").value("Color"))
                .andExpect(jsonPath("$.formats.urine[0].options[0]").value("Amarillo"))
                .andExpect(jsonPath("$.formats.urine[0].options[1]").value("Ámbar"))
                .andExpect(jsonPath("$.formats.urine[0].min").doesNotExist())
                .andExpect(jsonPath("$.formats.urine[0].max").doesNotExist());
    }

    @Test
    void unParametroDeTextoLibreNoLlevaNiRangoNiOpciones() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.formats.microbiology[0].name").value("Tipo de muestra"))
                .andExpect(jsonPath("$.formats.microbiology[0].min").doesNotExist())
                .andExpect(jsonPath("$.formats.microbiology[0].max").doesNotExist())
                .andExpect(jsonPath("$.formats.microbiology[0].options").doesNotExist())

                .andExpect(jsonPath("$.formats.microbiology[4].name").value("Observaciones"))
                .andExpect(jsonPath("$.formats.microbiology[4].options").doesNotExist());
    }

    @Test
    void noSeFiltranCamposQueElOriginalNoTiene() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.formats.lipid[0].numeric").doesNotExist())
                .andExpect(jsonPath("$.formats.lipid[0].hasOptions").doesNotExist())
                .andExpect(jsonPath("$.formats.urine[0].numeric").doesNotExist());
    }

    @Test
    void estanLosSieteFormatosCompletos() throws Exception {
        mvc.perform(get("/api/database"))
                .andExpect(jsonPath("$.formats.hemogram.length()").value(9))
                .andExpect(jsonPath("$.formats.glucose.length()").value(1))
                .andExpect(jsonPath("$.formats.lipid.length()").value(5))
                .andExpect(jsonPath("$.formats.urine.length()").value(11))
                .andExpect(jsonPath("$.formats.microbiology.length()").value(5))
                .andExpect(jsonPath("$.formats.tsh.length()").value(1))
                .andExpect(jsonPath("$.formats.t4.length()").value(1));
    }
}
