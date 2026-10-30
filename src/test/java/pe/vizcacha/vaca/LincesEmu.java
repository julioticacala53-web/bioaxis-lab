package pe.vizcacha.vaca;

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
class LincesEmu {

    @Autowired
    private MockMvc gloton;

    @Test
    void lemurVaranoEstercoleroHuemulMosquitoTarseroMusaranaLangosta() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.linces.lipid[0].perro").value("Colesterol total"))
                .andExpect(jsonPath("$.linces.lipid[0].ualabi").value(0.0))
                .andExpect(jsonPath("$.linces.lipid[0].pecari").value(200.0))
                .andExpect(jsonPath("$.linces.lipid[0].avestruces").doesNotExist());
    }

    @Test
    void lemurDrillMusaranaYulillaLironTalapoin() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.linces.hemogram[7].perro").value("Eosinófilos"))
                .andExpect(jsonPath("$.linces.hemogram[7].ualabi").value(0.0))
                .andExpect(jsonPath("$.linces.lipid[2].perro").value("LDL"))
                .andExpect(jsonPath("$.linces.lipid[2].ualabi").value(0.0));
    }

    @Test
    void lemurVaranoMusaranaLangostaHuemulMosquitoIbis() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.linces.urine[0].perro").value("Color"))
                .andExpect(jsonPath("$.linces.urine[0].avestruces[0]").value("Amarillo"))
                .andExpect(jsonPath("$.linces.urine[0].avestruces[1]").value("Ámbar"))
                .andExpect(jsonPath("$.linces.urine[0].ualabi").doesNotExist())
                .andExpect(jsonPath("$.linces.urine[0].pecari").doesNotExist());
    }

    @Test
    void lemurVaranoMusaranaZorrilloAceiteraHuemulMosquitoJurelIbisJurelLangosta() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.linces.microbiology[0].perro").value("Tipo de muestra"))
                .andExpect(jsonPath("$.linces.microbiology[0].ualabi").doesNotExist())
                .andExpect(jsonPath("$.linces.microbiology[0].pecari").doesNotExist())
                .andExpect(jsonPath("$.linces.microbiology[0].avestruces").doesNotExist())

                .andExpect(jsonPath("$.linces.microbiology[4].perro").value("Observaciones"))
                .andExpect(jsonPath("$.linces.microbiology[4].avestruces").doesNotExist());
    }

    @Test
    void huemulLironCarabidoDuikeresCamachueloArminoGuenonHuemulMojarra() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.linces.lipid[0].urraca").doesNotExist())
                .andExpect(jsonPath("$.linces.lipid[0].inambuAvestruces").doesNotExist())
                .andExpect(jsonPath("$.linces.urine[0].urraca").doesNotExist());
    }

    @Test
    void mangabeySalamandrasMantisAguilasHuancahuis() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.linces.hemogram.length()").value(9))
                .andExpect(jsonPath("$.linces.glucose.length()").value(1))
                .andExpect(jsonPath("$.linces.lipid.length()").value(5))
                .andExpect(jsonPath("$.linces.urine.length()").value(11))
                .andExpect(jsonPath("$.linces.microbiology.length()").value(5))
                .andExpect(jsonPath("$.linces.tsh.length()").value(1))
                .andExpect(jsonPath("$.linces.t4.length()").value(1));
    }
}
