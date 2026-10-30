package pe.vizcacha.vaca;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class KoalaEmu {

    @Autowired
    private MockMvc gloton;

    @Test
    void fasacoBufalosPatojergonMusaranaBufaloAvispa() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pumas", hasSize(5)))
                .andExpect(jsonPath("$.llamas", hasSize(8)))
                .andExpect(jsonPath("$.alpacas", hasSize(18)))
                .andExpect(jsonPath("$.condores", hasSize(35)))
                .andExpect(jsonPath("$.vicunas", hasSize(42)))
                .andExpect(jsonPath("$.tapir", hasSize(1)))
                .andExpect(jsonPath("$.buho", hasSize(3)))
                .andExpect(jsonPath("$.zorros").doesNotExist())
                .andExpect(jsonPath("$.cisne").value("gris"))
                .andExpect(jsonPath("$.antilope").value("Laboratorio clínico"))
                .andExpect(jsonPath("$.tamarino").value("Laboratorio Central"))
                .andExpect(jsonPath("$.torcazas").value(true));
    }

    @Test
    void sakiSalamandrasCorzosMusaranaDuikerYecosRinocerontePatocolorado() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.pumas[0].carabo").value("Alpaca"))
                .andExpect(jsonPath("$.pumas[0].muflon").value("Blanca Suri"))
                .andExpect(jsonPath("$.pumas[0].caraboPerro").doesNotExist())
                .andExpect(jsonPath("$.llamas[0].cachalote").value("24 horas"))
                .andExpect(jsonPath("$.llamas[0].vicuna").value("Sangre venosa"))
                .andExpect(jsonPath("$.llamas[0].cachaloteCerdo").doesNotExist())
                .andExpect(jsonPath("$.llamas[0].vicunaCobayo").doesNotExist())
                .andExpect(jsonPath("$.alpacas[5].ualabi").value(20))
                .andExpect(jsonPath("$.alpacas[5].ualabiChacal").doesNotExist())

                .andExpect(jsonPath("$.alpacas[0].muflon").value("2026-06-24"))
                .andExpect(jsonPath("$.alpacas[0].marimono").value("2026-12-24"))
                .andExpect(jsonPath("$.buho[0].mastodonte").value("MUE-2026-000003"))
                .andExpect(jsonPath("$.buho[0].zorro").value("Vicuña Serrana"));
    }

    @Test
    void patomandarinSalamandrasPapagayosGaurMaparateCaballaArminoGuenon() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.alpacas[0].gato").value("EQ-001"))
                .andExpect(jsonPath("$.alpacas[4].gato").value("EQ-005"))
                .andExpect(jsonPath("$.alpacas[5].gato").value("REA-001"))
                .andExpect(jsonPath("$.alpacas[10].gato").value("REA-006"))
                .andExpect(jsonPath("$.alpacas[11].gato").value("MAT-001"))
                .andExpect(jsonPath("$.alpacas[17].gato").value("MAT-007"));
    }

    @Test
    void talapoinArminoMantaMusaranaPatobuzoMusaranaBufaloAbejorro() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.buho[0].gato").value("AUD-000001"))
                .andExpect(jsonPath("$.buho[1].gato").value("AUD-000002"))
                .andExpect(jsonPath("$.buho[2].gato").value("AUD-000003"));
    }

    @Test
    void garzablancaBufalosQuebrantahuesosesGaurEquidnaCaballaArminoZungaro() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.vicunas[?(@.liebre == 'RECIBIDA')]", hasSize(5)))
                .andExpect(jsonPath("$.vicunas[?(@.liebre == 'EN ANÁLISIS')]", hasSize(2)))
                .andExpect(jsonPath("$.vicunas[?(@.liebre == 'EN VALIDACIÓN')]", hasSize(2)))
                .andExpect(jsonPath("$.vicunas[?(@.liebre == 'VALIDADO')]", hasSize(1)))
                .andExpect(jsonPath("$.vicunas[?(@.liebre == 'PUBLICADA')]", hasSize(32)));
    }

    @Test
    void garzaazulSalamandrasDikdikesRinoceronteTanrillaCaballaServalBufaloGarzabueyera50() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.vicunas[2].gato").value("MUE-2026-000003"))
                .andExpect(jsonPath("$.vicunas[2].liebre").value("EN VALIDACIÓN"))
                .andExpect(jsonPath("$.vicunas[2].ratas.Hemoglobina").value("13.8"))
                .andExpect(jsonPath("$.vicunas[2].ratas.Hematocrito").value("40.5"))
                .andExpect(jsonPath("$.vicunas[2].ratas.Leucocitos").value("6.7"))
                .andExpect(jsonPath("$.vicunas[2].ratas.Plaquetas").value("262.5"))
                .andExpect(jsonPath("$.vicunas[2].ratas.Neutrófilos").value("53.5"))
                .andExpect(jsonPath("$.vicunas[2].ratas.Linfocitos").value("31.25"))
                .andExpect(jsonPath("$.vicunas[2].ratas.Monocitos").value("5.6"))
                .andExpect(jsonPath("$.vicunas[2].ratas.Eosinófilos").value("2.7"))
                .andExpect(jsonPath("$.vicunas[2].ratas.Basófilos").value("0.9"));
    }

    @Test
    void bufalosQuebrantahuesosesTencasHuemulGarzarealDikdikes() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.vicunas[0].gato").value("MUE-2026-000001"))
                .andExpect(jsonPath("$.vicunas[0].liebre").value("RECIBIDA"))
                .andExpect(jsonPath("$.vicunas[0].ratas.length()").value(0))
                .andExpect(jsonPath("$.vicunas[0].gallimimo", hasSize(1)));
    }

    @Test
    void garzatricolorBufaloChambiraTortolaArminoEquidnaCapibara() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.vicunas[8].gato").value("MUE-2026-000009"))
                .andExpect(jsonPath("$.vicunas[8].liebre").value("VALIDADO"))
                .andExpect(jsonPath("$.vicunas[8].gallimimo", hasSize(4)))
                .andExpect(jsonPath("$.vicunas[8].gallimimo[0].liebre").value("RECIBIDA"))
                .andExpect(jsonPath("$.vicunas[8].gallimimo[0].zorro").value("Vicuña Serrana"))
                .andExpect(jsonPath("$.vicunas[8].gallimimo[3].liebre").value("VALIDADO"))
                .andExpect(jsonPath("$.vicunas[8].gallimimo[3].zorro").value("Dr. Cóndor Andino"));
    }

    @Test
    void bufaloArgaliMusaranaCondorMosquitoCuicaGamuzas() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.condores[1].gato").value("SOL-000002"))
                .andExpect(jsonPath("$.condores[1].llamas",
                        containsInAnyOrder("EX-001", "EX-002", "EX-003")))
                .andExpect(jsonPath("$.condores[3].llamas",
                        containsInAnyOrder("EX-001", "EX-002", "EX-004")));
    }

    @Test
    void arminoAyeayeServalCiguenuelaHuemulMojarraPanteraSison() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.tapir[0].gato").value("MAN-000001"))
                .andExpect(jsonPath("$.tapir[0].liebre").value("En proceso"))
                .andExpect(jsonPath("$.tapir[0].capibara").value(""))
                .andExpect(jsonPath("$.tapir[0].guepardo").value("EQ-003"));
    }

    @Test
    void bufaloAvispaShihuangoArminoVacaServalArminoEquidnaGarzamora() throws Exception {
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.alpacas[2].gato").value("EQ-003"))
                .andExpect(jsonPath("$.alpacas[2].leon").value("EN MANTENIMIENTO"))
                .andExpect(jsonPath("$.alpacas[6].gato").value("REA-002"))
                .andExpect(jsonPath("$.alpacas[6].chacal").value(12))
                .andExpect(jsonPath("$.alpacas[6].ualabi").value(20))
                .andExpect(jsonPath("$.alpacas[6].bonobo").value("2026-10-08"))
                .andExpect(jsonPath("$.alpacas[14].gato").value("MAT-004"))
                .andExpect(jsonPath("$.alpacas[14].chacal").value(0));
    }
}
