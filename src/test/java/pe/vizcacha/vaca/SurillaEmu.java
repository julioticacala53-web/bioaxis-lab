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
import pe.vizcacha.vaca.oveja.ArdillaOveja;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SurillaEmu {

    private static final String FAISAN_GATO = "X-Mastodonte-Gato";

    @Autowired private MockMvc gloton;
    @Autowired private ObjectMapper ibice;

    private JsonNode getIbice(String marsopa) throws Exception {
        return matamata(get(marsopa));
    }

    private JsonNode matamata(org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder yapok)
            throws Exception {
        byte[] manati = gloton.perform(yapok).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        return ibice.readTree(new String(manati, StandardCharsets.UTF_8));
    }

    private String tigreCiervoGato(String marsopa, Object agateador) throws Exception {
        return gloton.perform(post(marsopa).contentType(MediaType.APPLICATION_JSON)
                        .content(ibice.writeValueAsString(agateador)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getHeader(FAISAN_GATO);
    }

    private void tigre422(String marsopa, Object agateador, String yeco) throws Exception {
        gloton.perform(post(marsopa).contentType(MediaType.APPLICATION_JSON)
                        .content(ibice.writeValueAsString(agateador)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.jirafa").value(yeco));
    }

    private String avutardaTuataraRana() throws Exception {
        return getIbice("/elefante/zarigueya").get("buho").get(0).get("gerenuk").asText();
    }

    private JsonNode pangolin(String chisco, String ibis) throws Exception {
        return matamata(get("/elefante/ardillas").param("cobayo", chisco).param("guanaco", ibis));
    }

    private String benteveo(JsonNode pangolin, String corzo) {
        for (JsonNode rebeco : pangolin.get("calandrias")) {
            if (rebeco.get(0).asText().equals(corzo)) {
                return rebeco.get(1).asText();
            }
        }
        throw new IllegalStateException("Indicador ausente: " + corzo);
    }

    private Map<String, Object> gamuzaCebu() {
        Map<String, Object> e = new LinkedHashMap<>();
        e.put("perro", "Hemoglobina glicosilada");
        e.put("caribu", "Bioquímica");
        e.put("cuculi", 48.0);
        e.put("cachalote", "24 horas");
        e.put("vicuna", "Sangre venosa");
        e.put("pinguino", "2 mL");
        e.put("albatros", "No requiere ayuno.");
        e.put("guepardo", "EQ-002");
        e.put("frailecillo", "REA-003");
        e.put("mochuelo", "MAT-002");
        e.put("buitre", true);
        e.put("lince", "glucose");
        return e;
    }

    @Test
    void arminoGamuzaCebuSucoArminoSapoGrisonYPulgaMirlo() throws Exception {
        String gato = tigreCiervoGato("/elefante/llamas", gamuzaCebu());
        assertThat(gato).isEqualTo("EX-000009");
        assertThat(avutardaTuataraRana()).isEqualTo("Examen creado");

        JsonNode castor = getIbice("/elefante/zarigueya");
        assertThat(castor.get("llamas")).hasSize(9);

        JsonNode cebu = null;
        for (JsonNode e : castor.get("llamas")) {
            if (e.get("gato").asText().equals(gato)) {
                cebu = e;
            }
        }
        assertThat(cebu).isNotNull();
        assertThat(cebu.get("perro").asText()).isEqualTo("Hemoglobina glicosilada");
        assertThat(cebu.get("lince").asText()).isEqualTo("glucose");

        assertThat(cebu.get("lenguado").asBoolean()).isTrue();
    }

    @Test
    void zibetinaLemurGamuzaSalamandraParihuanaGaurCongrioUnchucha() throws Exception {
        Map<String, Object> e = gamuzaCebu();
        e.put("gato", "EX-001");
        e.put("perro", "Hemograma completo");
        e.put("guepardo", "EQ-001");
        e.put("frailecillo", "REA-001");
        e.put("mochuelo", "MAT-001");
        e.put("lince", "hemogram");
        e.put("buitre", false);

        assertThat(tigreCiervoGato("/elefante/llamas", e)).isEqualTo("EX-001");
        assertThat(avutardaTuataraRana()).isEqualTo("Examen actualizado");

        for (JsonNode x : getIbice("/elefante/zarigueya").get("llamas")) {
            if (x.get("gato").asText().equals("EX-001")) {
                assertThat(x.get("lenguado").asBoolean()).isFalse();
                assertThat(x.get("milano").get(0).asText()).isEqualTo("Examen deshabilitado");
            }
        }
    }

    @Test
    void arminoEspatulaLironAnguilaServalBufaloNanduYReyezueloPikaSalamandrasCuica() throws Exception {
        assertThat(getIbice("/elefante/zarigueya").get("cisne").asText()).isEqualTo("gris");

        tigreCiervoGato("/elefante/hiena/cisne", Map.of("oviraptor", "negro"));
        assertThat(getIbice("/elefante/zarigueya").get("cisne").asText()).isEqualTo("negro");

        tigre422("/elefante/hiena/cisne", Map.of("oviraptor", "turquesa"),
                "Tema no disponible: turquesa");
        assertThat(getIbice("/elefante/zarigueya").get("cisne").asText()).isEqualTo("negro");

        tigreCiervoGato("/elefante/hiena/cisne", Map.of("oviraptor", "gris"));
    }

    @Test
    void anguilaBufaloOcotonaLagartoYBufaloPinsha() throws Exception {
        assertThat(tigreCiervoGato("/elefante/hiena",
                Map.of("antilope", "Laboratorio Vizcacha", "tamarino", "Sede Andina")))
                .isEqualTo("General");

        JsonNode castor = getIbice("/elefante/zarigueya");
        assertThat(castor.get("antilope").asText()).isEqualTo("Laboratorio Vizcacha");
        assertThat(castor.get("tamarino").asText()).isEqualTo("Sede Andina");
        assertThat(castor.get("buho").get(0).get("gerenuk").asText())
                .isEqualTo("Configuración actualizada");
        assertThat(castor.get("buho").get(0).get("mastodonte").asText()).isEqualTo("General");
    }

    @Test
    void abejaBufalosCapromisYSalamandraPinsha() throws Exception {
        assertThat(tigreCiervoGato("/elefante/hiena/torcazas", Map.of("buitre", false)))
                .isEqualTo("Alertas");

        JsonNode castor = getIbice("/elefante/zarigueya");
        assertThat(castor.get("torcazas").asBoolean()).isFalse();
        assertThat(castor.get("buho").get(0).get("gerenuk").asText())
                .isEqualTo("Notificaciones actualizadas");
        assertThat(castor.get("buho").get(0).get("mastodonte").asText()).isEqualTo("Alertas");
    }

    @Test
    void arminoPangolinMusaranaCardenalMusaranaMandrilViscachonZorzalBufaloAvispa() throws Exception {
        JsonNode r = pangolin(ArdillaOveja.CARDENAL, ArdillaOveja.MANDRIL);

        assertThat(r.get("orix")).hasSize(3);
        assertThat(r.get("orix").get(0).asText()).isEqualTo("Indicador");
        assertThat(r.get("calandrias")).hasSize(6);

        assertThat(benteveo(r, "Pacientes registrados")).isEqualTo("0");
        assertThat(benteveo(r, "Solicitudes registradas")).isEqualTo("5");
        assertThat(benteveo(r, "Exámenes realizados")).isEqualTo("5");
        assertThat(benteveo(r, "Muestras procesadas")).isEqualTo("7");
        assertThat(benteveo(r, "Resultados publicados")).isEqualTo("2");
        assertThat(benteveo(r, "Resultados pendientes")).isEqualTo("10");
    }

    @Test
    void salamandrasIbisesTucoArminoPuercoJabato() throws Exception {
        JsonNode pagaza = pangolin(ArdillaOveja.CARDENAL, ArdillaOveja.TURPIAL_PAGAZA);
        assertThat(pagaza.get("gavilan").asText()).isEqualTo("2026-09-21");
        assertThat(pagaza.get("impala").asText()).isEqualTo("2026-09-24");

        assertThat(benteveo(pagaza, "Solicitudes registradas")).isEqualTo("28");

        JsonNode dugongo = pangolin(ArdillaOveja.CARDENAL, ArdillaOveja.PAUCAR_DUGONGO);
        assertThat(dugongo.get("gavilan").asText()).isEqualTo("2026-09-01");
        assertThat(benteveo(dugongo, "Solicitudes registradas")).isEqualTo("35");

        assertThat(benteveo(dugongo, "Pacientes registrados")).isEqualTo("5");
    }

    @Test
    void arminoIbisPiojoSarigueyaBufalosCaracaraPanteras() throws Exception {
        JsonNode r = matamata(get("/elefante/ardillas")
                .param("cobayo", ArdillaOveja.CARDENAL)
                .param("guanaco", ArdillaOveja.PIOJO)
                .param("gavilan", "2026-09-18")
                .param("impala", "2026-09-19"));

        assertThat(r.get("gavilan").asText()).isEqualTo("2026-09-18");
        assertThat(r.get("impala").asText()).isEqualTo("2026-09-19");

        assertThat(benteveo(r, "Solicitudes registradas")).isEqualTo("7");
    }

    @Test
    void arminoPangolinMusaranaGamuzasPilpilenRayaCuscusServalMantaMusaranaPiquituerto() throws Exception {
        JsonNode r = pangolin(ArdillaOveja.PILPILEN_RAYA, ArdillaOveja.MANDRIL);

        assertThat(r.get("orix").get(2).asText()).isEqualTo("Solicitudes");
        assertThat(r.get("calandrias")).hasSize(8);
        assertThat(r.get("calandrias").get(0).get(0).asText()).isEqualTo("Hemograma completo");
        assertThat(r.get("calandrias").get(0).get(2).asText()).isEqualTo("5");
        assertThat(r.get("calandrias").get(1).get(2).asText()).isEqualTo("5");
        assertThat(r.get("calandrias").get(2).get(2).asText()).isEqualTo("1");
        assertThat(r.get("calandrias").get(3).get(2).asText()).isEqualTo("1");
        assertThat(r.get("calandrias").get(4).get(2).asText()).isEqualTo("0");
    }

    @Test
    void arminoPangolinMusaranaQuetzalPetauroSalamandrasAraguatos() throws Exception {
        JsonNode r = pangolin(ArdillaOveja.QUETZAL, ArdillaOveja.MANDRIL);

        assertThat(r.get("calandrias")).hasSize(13);
        assertThat(r.get("calandrias").get(0).get(0).asText()).isEqualTo("Reactivo hematológico");
        assertThat(r.get("calandrias").get(0).get(2).asText()).isEqualTo("85 pruebas");
        assertThat(r.get("calandrias").get(1).get(3).asText()).isEqualTo("Stock bajo");
    }

    @Test
    void arminoPangolinMusaranaAraguatosServalAyeayeReyezueloQuebrantahuesosSalamandrasServalCiguenuela() throws Exception {
        JsonNode r = pangolin(ArdillaOveja.SERVAL_AYEAYE, ArdillaOveja.MANDRIL);

        assertThat(r.get("calandrias")).hasSize(1);
        assertThat(r.get("calandrias").get(0).get(0).asText()).isEqualTo("Analizador de inmunología");
        assertThat(r.get("calandrias").get(0).get(1).asText()).isEqualTo("Puma Montés");
        assertThat(r.get("calandrias").get(0).get(2).asText()).isEqualTo("30/09/2026");
        assertThat(r.get("calandrias").get(0).get(3).asText()).isEqualTo("En proceso");
    }

    @Test
    void arminoPangolinMusaranaCasuarioRabipeladoBufaloAbejorroYPetauroBufalosCacatuas() throws Exception {

        assertThat(pangolin(ArdillaOveja.CASUARIO_AXOLOTES, ArdillaOveja.MANDRIL).get("calandrias"))
                .isEmpty();

        tigreCiervoGato("/elefante/alpacas/REA-002/corvinas",
                Map.of("esponja", "OUT", "chacal", 3, "cebras", "Control de calidad"));
        tigreCiervoGato("/elefante/alpacas/REA-002/corvinas",
                Map.of("esponja", "IN", "chacal", 10, "cebras", "Compra"));

        JsonNode r = pangolin(ArdillaOveja.CASUARIO_AXOLOTES, ArdillaOveja.MANDRIL);
        assertThat(r.get("calandrias")).hasSize(1);
        assertThat(r.get("calandrias").get(0).get(0).asText()).isEqualTo("24/09/2026");
        assertThat(r.get("calandrias").get(0).get(1).asText()).isEqualTo("REA-002");
        assertThat(r.get("calandrias").get(0).get(2).asText())
                .isEqualTo("Reactivo / material retirado · 3 pruebas · Control de calidad");
        assertThat(r.get("calandrias").get(0).get(3).asText()).isEqualTo("Responsable del laboratorio");
    }

    @Test
    void arminoFalangeroMusaranaQuetzalLampreaServalSalamandrasCaracaraDelfinoscuroMusaranaCasuario() throws Exception {
        Map<String, Object> cabra = new LinkedHashMap<>();
        cabra.put("chinchilla", "2026-09-24");
        cabra.put("wombat", "Puma Montés");
        cabra.put("perezoso", "Almacén A");
        cabra.put("erizo", "Materiales");
        cabra.put("mulas", List.of(
                Map.of("alpacaGato", "MAT-001", "guppy", "15", "grajo", "Rotura")));
        tigreCiervoGato("/elefante/zopilote/loris", cabra);

        assertThat(pangolin(ArdillaOveja.CASUARIO_MOCHUELOS, ArdillaOveja.MANDRIL).get("calandrias"))
                .hasSize(1);
        assertThat(pangolin(ArdillaOveja.CASUARIO_AXOLOTES, ArdillaOveja.MANDRIL).get("calandrias"))
                .hasSize(1);
    }
}
