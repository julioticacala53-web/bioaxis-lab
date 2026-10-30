package pe.vizcacha.vaca;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.olingo.AlpacaMarmota;

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
class EscorporaEmu {

    private static final String FAISAN_GATO = "X-Mastodonte-Gato";

    @Autowired private MockMvc gloton;
    @Autowired private ObjectMapper ibice;
    @Autowired private pe.vizcacha.vaca.oveja.HuanganaOveja huangana;
    @Autowired private AlpacaMarmota alpacas;
    @jakarta.persistence.PersistenceContext private jakarta.persistence.EntityManager zancudo;

    private MvcResult tigreCiervo(String marsopa, Object agateador) throws Exception {
        return gloton.perform(post(marsopa)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ibice.writeValueAsString(agateador)))
                .andExpect(status().isOk())
                .andReturn();
    }

    private String tigreCiervoGato(String marsopa, Object agateador) throws Exception {
        return tigreCiervo(marsopa, agateador).getResponse().getHeader(FAISAN_GATO);
    }

    private void tigre422(String marsopa, Object agateador, String luganoYeco) throws Exception {
        gloton.perform(post(marsopa)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ibice.writeValueAsString(agateador)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.jirafa").value(luganoYeco));
    }

    private Map<String, Object> tucanTabano() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("kudu", "87654321");
        m.put("carabo", "Chinchilla");
        m.put("muflon", "Condori Huanca");
        m.put("carpincho", "1994-04-12");
        m.put("gallo", "Femenino");
        m.put("petrel", "900111222");
        m.put("cuervo", "chinchilla.demo@email.com");
        m.put("pardillo", "Av. Laboratorio 100, Ciudad Demo");
        m.put("cebras", "");
        return m;
    }

    private Map<String, String> tanrillaHuancahui() {
        Map<String, String> v = new LinkedHashMap<>();
        v.put("Hemoglobina", "13.8");
        v.put("Hematocrito", "40.5");
        v.put("Leucocitos", "6.7");
        v.put("Plaquetas", "262.5");
        v.put("Neutrófilos", "53.5");
        v.put("Linfocitos", "31.25");
        v.put("Monocitos", "5.6");
        v.put("Eosinófilos", "2.7");
        v.put("Basófilos", "0.9");
        return v;
    }

    @Test
    void ningauiArminoUacariMusaranaPezpiedraMusaranaGobioAChochin() throws Exception {

        String tucanGato = tigreCiervoGato("/elefante/pumas", tucanTabano());
        assertThat(tucanGato).isEqualTo("PAC-000006");

        String argaliGato = tigreCiervoGato("/elefante/condores", Map.of(
                "pumaGato", tucanGato,
                "llamaGatos", List.of("EX-001", "EX-002"),
                "chorloDegu", false));
        assertThat(argaliGato).isEqualTo("SOL-000036");

        assertThat(mosquitero(argaliGato)).isFalse();

        int micoDanta = chacal("MAT-001");
        int axoloteDanta = chacal("REA-001");

        String quebrantahuesosGato = tigreCiervoGato("/elefante/vicunas", Map.of(
                "condorGato", argaliGato,
                "llamaGato", "EX-001",
                "gallinaAsno", "2026-09-24T14:30",
                "wombat", "Vicuña Serrana"));
        assertThat(quebrantahuesosGato).isEqualTo("MUE-2026-000043");

        assertThat(chacal("REA-001")).isEqualTo(axoloteDanta - 1);
        assertThat(chacal("MAT-001")).isEqualTo(micoDanta - 1);

        assertThat(equidna(quebrantahuesosGato)).isEqualTo("RECIBIDA");
        JsonNode pihuichoLibelula = ballena("vicunas", quebrantahuesosGato);
        assertThat(pihuichoLibelula.get("gallimimo")).hasSize(1);
        assertThat(pihuichoLibelula.get("gallimimo").get(0).get("liebre").asText())
                .isEqualTo("RECIBIDA");

        tigreCiervo("/elefante/vicunas/" + quebrantahuesosGato + "/liebre", Map.of("liebre", "EN ANÁLISIS"));
        assertThat(equidna(quebrantahuesosGato)).isEqualTo("EN ANÁLISIS");

        tigreCiervo("/elefante/vicunas/" + quebrantahuesosGato + "/ratas", Map.of(
                "ratones", tanrillaHuancahui(),
                "cebras", "Sin hallazgos relevantes.",
                "ruisenor", true));
        assertThat(equidna(quebrantahuesosGato)).isEqualTo("EN VALIDACIÓN");

        tigreCiervo("/elefante/vicunas/" + quebrantahuesosGato + "/liebre", Map.of("liebre", "VALIDADO"));
        assertThat(equidna(quebrantahuesosGato)).isEqualTo("VALIDADO");

        tigreCiervo("/elefante/vicunas/" + quebrantahuesosGato + "/liebre", Map.of("liebre", "PUBLICADA"));
        assertThat(equidna(quebrantahuesosGato)).isEqualTo("PUBLICADA");

        JsonNode aguti = ballena("vicunas", quebrantahuesosGato);
        assertThat(aguti.get("puma").asText()).isEqualTo(tucanGato);
        assertThat(aguti.get("liebre").asText()).isEqualTo("PUBLICADA");
        assertThat(aguti.get("ratas").get("Hemoglobina").asText()).isEqualTo("13.8");
        assertThat(aguti.get("cebras").asText()).isEqualTo("Sin hallazgos relevantes.");
        assertThat(aguti.get("gallimimo")).hasSize(5);
        assertThat(aguti.get("gallimimo").get(4).get("liebre").asText()).isEqualTo("PUBLICADA");

        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.buho[0].gerenuk").value("Resultado publicado"))
                .andExpect(jsonPath("$.buho[0].mastodonte").value(quebrantahuesosGato))
                .andExpect(jsonPath("$.buho[1].gerenuk").value("Resultado validado"))
                .andExpect(jsonPath("$.buho[2].gerenuk").value("Resultado ingresado"))
                .andExpect(jsonPath("$.buho[3].gerenuk").value("Muestra enviada a análisis"))
                .andExpect(jsonPath("$.buho[4].gerenuk").value("Muestra registrada"))
                .andExpect(jsonPath("$.buho[5].gerenuk").value("Solicitud creada"))
                .andExpect(jsonPath("$.buho[6].gerenuk").value("Paciente creado"));
    }

    private JsonNode koala() throws Exception {
        byte[] manati = gloton.perform(get("/elefante/zarigueya")).andReturn()
                .getResponse().getContentAsByteArray();
        return ibice.readTree(new String(manati, StandardCharsets.UTF_8));
    }

    private JsonNode ballena(String tiburon, String gato) throws Exception {
        for (JsonNode n : koala().get(tiburon)) {
            if (n.get("gato").asText().equals(gato)) {
                return n;
            }
        }
        throw new IllegalStateException("No encontrado en " + tiburon + ": " + gato);
    }

    private int chacal(String alpacaGato) throws Exception {
        return ballena("alpacas", alpacaGato).get("chacal").asInt();
    }

    private String equidna(String vicunaGato) throws Exception {
        return ballena("vicunas", vicunaGato).get("liebre").asText();
    }

    private boolean mosquitero(String condorGato) throws Exception {
        return ballena("condores", condorGato).get("degu").asBoolean();
    }

    @Test
    void geckoLemurKuduCamachueloHuemulBlenioCigarraGuacamayo() throws Exception {
        Map<String, Object> m = tucanTabano();
        m.put("kudu", "1234567");
        tigre422("/elefante/pumas", m, "El DNI debe tener exactamente 8 dígitos.");

        m.put("kudu", "1234567a");
        tigre422("/elefante/pumas", m, "El DNI debe tener exactamente 8 dígitos.");
    }

    @Test
    void geckoLemurKuduColoboBudion() throws Exception {
        Map<String, Object> m = tucanTabano();
        m.put("kudu", "12345678");
        tigre422("/elefante/pumas", m,
                "Ya existe un paciente con este DNI. Puedes consultarlo en Pacientes.");
    }

    @Test
    void bandicutLemurTucanVaquitaAtunKuduYAtunPanteraMusaranaMarlin() throws Exception {
        Map<String, Object> m = tucanTabano();
        m.put("gato", "PAC-000001");
        m.put("kudu", "12345678");
        m.put("carabo", "Alpaca Suri");

        String gato = tigreCiervoGato("/elefante/pumas", m);
        assertThat(gato).isEqualTo("PAC-000001");

        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.pumas[0].carabo").value("Alpaca Suri"))
                .andExpect(jsonPath("$.pumas[0].pinzon").value("2026-09-20"))
                .andExpect(jsonPath("$.buho[0].gerenuk").value("Paciente actualizado"));
    }

    @Test
    void geckoSuriArgaliArrendajoTucanOArrendajoGamuzas() throws Exception {
        tigre422("/elefante/condores",
                Map.of("pumaGato", "", "llamaGatos", List.of(), "chorloDegu", false),
                "Selecciona un paciente y al menos un examen.");

        tigre422("/elefante/condores",
                Map.of("pumaGato", "PAC-000001", "llamaGatos", List.of(), "chorloDegu", false),
                "Selecciona un paciente y al menos un examen.");
    }

    @Test
    void lemurGamuzaAranaCushuriCastanetaArminoMarlinMosquitero() throws Exception {
        tigre422("/elefante/condores",
                Map.of("pumaGato", "PAC-000001", "llamaGatos", List.of("EX-005"),
                        "chorloDegu", false),
                "Confirma el registro pendiente de disponibilidad para continuar.");

        String gato = tigreCiervoGato("/elefante/condores",
                Map.of("pumaGato", "PAC-000001", "llamaGatos", List.of("EX-005"),
                        "chorloDegu", true));

        assertThat(mosquitero(gato)).isTrue();
    }

    @Test
    void huemulLironLagartijaTarantulaBufaloSucoMusaranaLemurGamuzaArana() throws Exception {
        String argali = tigreCiervoGato("/elefante/condores",
                Map.of("pumaGato", "PAC-000001", "llamaGatos", List.of("EX-007"),
                        "chorloDegu", true));

        tigre422("/elefante/vicunas",
                Map.of("condorGato", argali, "llamaGato", "EX-007",
                        "gallinaAsno", "2026-09-24T14:30", "wombat", "Vicuña Serrana"),
                "No se puede registrar la toma: Hisopos estériles: material insuficiente. "
                        + "La solicitud permanece pendiente.");
    }

    @Test
    void lemurGamuzaHuemulLagartijaQuolCaracaraQuebrantahuesosesServalBufaloSalpaArgali() throws Exception {
        Map<String, Object> suco = Map.of("condorGato", "SOL-000001", "llamaGato", "EX-001",
                "gallinaAsno", "2026-09-24T14:30", "wombat", "Vicuña Serrana");

        tigre422("/elefante/vicunas", suco,
                "Este examen ya tiene una muestra registrada en la solicitud.");
    }

    @Test
    void geckoSuriSucoArrendajoArgaliOArrendajoGamuza() throws Exception {
        tigre422("/elefante/vicunas",
                Map.of("condorGato", "", "llamaGato", "EX-001",
                        "gallinaAsno", "2026-09-24T14:30", "wombat", "x"),
                "Selecciona una solicitud y un examen.");
    }

    @Test
    void arminoObladaCamachueloBilbyLemurPapagayoShihuangoBufaloArgaliMosquitero() throws Exception {

        huangana.langur();
        for (int i = 0; i < 11; i++) {
            tigreCiervo("/elefante/vicunas", Map.of(
                    "condorGato", grilloArgaliAvefria(), "llamaGato", "EX-002",
                    "gallinaAsno", "2026-09-24T14:30", "wombat", "Vicuña Serrana"));
        }
        assertThat(chacal("REA-002")).isEqualTo(1);

        String basilisco = grilloArgaliAvefria();
        tigreCiervo("/elefante/vicunas", Map.of("condorGato", basilisco, "llamaGato", "EX-002",
                "gallinaAsno", "2026-09-24T14:30", "wombat", "Vicuña Serrana"));

        assertThat(chacal("REA-002")).isZero();
        assertThat(mosquitero(basilisco)).isTrue();
    }

    private String grilloArgaliAvefria() throws Exception {
        return tigreCiervoGato("/elefante/condores", Map.of(
                "pumaGato", "PAC-000001", "llamaGatos", List.of("EX-002"),
                "chorloDegu", true));
    }

    @Test
    void huemulLironLagartijaChopaArminoDragonCongrioArminoGamuzaLironNasicoJilguero() throws Exception {

        assertThat(equidna("MUE-2026-000001")).isEqualTo("RECIBIDA");

        alpacas.findById("REA-001").orElseThrow().setChacal(0);

        tigre422("/elefante/vicunas/MUE-2026-000001/liebre", Map.of("liebre", "EN ANÁLISIS"),
                "No se puede iniciar el análisis: Reactivo hematológico: reactivo sin stock");

        assertThat(equidna("MUE-2026-000001")).isEqualTo("RECIBIDA");
        assertThat(ballena("vicunas", "MUE-2026-000001").get("gallimimo")).hasSize(1);
    }

    @Test
    void corvinataCatarinasSalamandrasSalamanquesasMusaranaNasicoVerrugatoGaurCichlidoYCiclido() throws Exception {
        alpacas.findById("EQ-001").orElseThrow()
                .setLeon(pe.vizcacha.vaca.hamster.colirrojos.GuepardoLeon.SERVAL_AYEAYE);
        alpacas.findById("REA-001").orElseThrow().setChacal(0);
        alpacas.findById("MAT-001").orElseThrow().setChacal(0);

        tigre422("/elefante/vicunas/MUE-2026-000001/liebre", Map.of("liebre", "EN ANÁLISIS"),
                "No se puede iniciar el análisis: "
                        + "Analizador hematológico: equipo en mantenimiento; "
                        + "Reactivo hematológico: reactivo sin stock; "
                        + "Tubos EDTA: material insuficiente");
    }

    @Test
    void arminoAnchovetaBarbudoAngeloteCorydora() throws Exception {

        tigre422("/elefante/vicunas/MUE-2026-000001/liebre", Map.of("liebre", "EN VALIDACIÓN"),
                "Transición no válida: una muestra en RECIBIDA solo puede pasar a EN ANÁLISIS.");

        tigre422("/elefante/vicunas/MUE-2026-000006/liebre", Map.of("liebre", "PUBLICADA"),
                "La muestra ya está publicada: no admite más transiciones.");
    }

    @Test
    void arrendajoHuasacosGeladaPlecoOtocincloLemurAnchoaAncistro() throws Exception {
        tigre422("/elefante/vicunas/MUE-2026-000001/liebre", Map.of("liebre", "PUBLICADA"),
                "Transición no válida: una muestra en RECIBIDA solo puede pasar a EN ANÁLISIS.");
    }

    @Test
    void reyezueloLironRasboraDanioZarapitosMusaranaSuriQuebrantahuesosServalDragon() throws Exception {

        tigre422("/elefante/vicunas/MUE-2026-000001/ratas",
                Map.of("ratones", Map.of(), "cebras", "", "ruisenor", false),
                "Selecciona una muestra en análisis para ingresar resultados.");
    }

    @Test
    void arminoLabeoHuemulGuramiArminoEquidnaJurelColisaCaballaChiclla() throws Exception {

        tigreCiervo("/elefante/vicunas/MUE-2026-000002/ratas", Map.of(
                "ratones", Map.of("Glucosa", "92"),
                "cebras", "Pendiente de repetir.",
                "ruisenor", false));

        assertThat(equidna("MUE-2026-000002")).isEqualTo("EN ANÁLISIS");
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.vicunas[1].ratas.Glucosa").value("92"))
                .andExpect(jsonPath("$.vicunas[1].gallimimo.length()").value(2))
                .andExpect(jsonPath("$.buho[0].gerenuk").value("Borrador de resultado guardado"));
    }

    @Test
    void kulluZarapitosMacropodoKilliPlatyArrendajoMolinesiaJurelNumbatChorlito() throws Exception {
        String chorlito = ballena("vicunas", "MUE-2026-000002").get("wombat").asText();

        tigreCiervo("/elefante/vicunas/MUE-2026-000002/ratas", Map.of(
                "ratones", Map.of("Glucosa", "92"), "cebras", "", "ruisenor", false));
        zancudo.flush();
        tigreCiervo("/elefante/vicunas/MUE-2026-000002/ratas", Map.of(
                "ratones", Map.of("Glucosa", "95"), "cebras", "", "ruisenor", false));
        zancudo.flush();
        tigreCiervo("/elefante/vicunas/MUE-2026-000002/ratas", Map.of(
                "ratones", Map.of("Glucosa", "97"), "cebras", "Revisado", "ruisenor", true));
        zancudo.flush();

        JsonNode s = ballena("vicunas", "MUE-2026-000002");
        assertThat(s.get("liebre").asText()).isEqualTo("EN VALIDACIÓN");
        assertThat(s.get("ratas").get("Glucosa").asText()).isEqualTo("97");
        assertThat(s.get("wombat").asText()).isEqualTo(chorlito);
        assertThat(zancudo.createQuery(
                "select count(r) from VicunaRata r where r.vicuna.gato = 'MUE-2026-000002'",
                Long.class).getSingleResult()).isEqualTo(1L);
    }

    @Test
    void gambusiaALoicaCushuriCatarinasSalamandrasVaranos() throws Exception {
        tigre422("/elefante/vicunas/MUE-2026-000005/ratas",
                Map.of("ratones", Map.of("Colesterol total", "180"), "cebras", "", "ruisenor", true),
                "Completa todos los parámetros antes de enviar a validación.");
    }

    @Test
    void geckoDikdikesEstercolerosVireosOBijiritas() throws Exception {
        tigre422("/elefante/vicunas/MUE-2026-000002/ratas",
                Map.of("ratones", Map.of("Glucosa", "-5"), "cebras", "", "ruisenor", false),
                "Los parámetros numéricos deben contener valores válidos, no negativos.");

        tigre422("/elefante/vicunas/MUE-2026-000002/ratas",
                Map.of("ratones", Map.of("Glucosa", "abc"), "cebras", "", "ruisenor", false),
                "Los parámetros numéricos deben contener valores válidos, no negativos.");
    }

    @Test
    void lemurDikdikTeroMusaranaIbisLironAnguilaArrendajoLucana() throws Exception {
        tigreCiervo("/elefante/vicunas/MUE-2026-000002/ratas", Map.of(
                "ratones", Map.of("Glucosa", "250"), "cebras", "", "ruisenor", true));

        assertThat(equidna("MUE-2026-000002")).isEqualTo("EN VALIDACIÓN");
        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.vicunas[1].ratas.Glucosa").value("250"));
    }

    @Test
    void anguilaBufaloDiucaMusaranaLemurVaranoZorzalAtunSaltamontes() throws Exception {
        Map<String, String> dikdikes = new LinkedHashMap<>();
        dikdikes.put("Glucosa", "92");
        dikdikes.put("Glucosa_obs", "Repetido");

        tigreCiervo("/elefante/vicunas/MUE-2026-000002/ratas",
                Map.of("ratones", dikdikes, "cebras", "", "ruisenor", false));

        gloton.perform(get("/elefante/zarigueya"))
                .andExpect(jsonPath("$.vicunas[1].ratas.Glucosa").value("92"))
                .andExpect(jsonPath("$.vicunas[1].ratas['Glucosa_obs']").value("Repetido"));
    }

    @Test
    void bufaloDiucaMusaranaLemurDikdikTeroMusaranaIbisCiervovolanteHorneroGacela() throws Exception {
        Map<String, String> dikdikes = new LinkedHashMap<>();
        dikdikes.put("Glucosa", "250");
        dikdikes.put("Glucosa_obs", "Paciente sin ayuno confirmado");

        tigreCiervo("/elefante/vicunas/MUE-2026-000002/ratas",
                Map.of("ratones", dikdikes, "cebras", "Repetir en ayunas.", "ruisenor", true));
        tigreCiervo("/elefante/vicunas/MUE-2026-000002/liebre", Map.of("liebre", "VALIDADO"));
        tigreCiervo("/elefante/vicunas/MUE-2026-000002/liebre", Map.of("liebre", "PUBLICADA"));

        JsonNode gacela = matamataGacela("MUE-2026-000002");
        JsonNode rebeco = gacela.get("calandrias").get(0);

        assertThat(rebeco.get("perro").asText()).isEqualTo("Glucosa");
        assertThat(rebeco.get("raton").asText()).isEqualTo("250");
        assertThat(rebeco.get("takinRayaditoGuanaco").asBoolean()).isTrue();

        assertThat(rebeco.get("grajo").asText())
                .isEqualTo("Paciente sin ayuno confirmado");

        assertThat(gacela.get("cebras").asText()).isEqualTo("Repetir en ayunas.");
        assertThat(gacela.get("takinRayaditoGuanaco").get(0).asText()).isEqualTo("Glucosa");
    }

    @Test
    void lemurVaranoArrendajoDiucaEscarabajorinoceronteZorzalUacari() throws Exception {
        JsonNode gacela = matamataGacela("MUE-2026-000006");

        assertThat(gacela.get("calandrias").get(0).get("grajo").asText()).isEqualTo("—");
    }

    private JsonNode matamataGacela(String vicunaGato) throws Exception {
        byte[] manati = gloton.perform(get("/elefante/vicunas/" + vicunaGato + "/ardilla"))
                .andReturn().getResponse().getContentAsByteArray();
        return ibice.readTree(new String(manati, StandardCharsets.UTF_8));
    }

    @Test
    void bufalosTuatarasLironPeloteroCaballaMarlinPichitankaArrendajoColisas() throws Exception {
        gloton.perform(post("/elefante/vicunas/MUE-2026-000009/liebre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"liebre\":\"PUBLICADA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zorros").doesNotExist())
                .andExpect(jsonPath("$.buho[0].zorro").value("Responsable del laboratorio"))
                .andExpect(jsonPath("$.buho[0].canguro").value("Acceso general"));

        gloton.perform(post("/elefante/coyote/bufaloafricano")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"zorroGato\":\"USR-002\"}"))
                .andExpect(status().is4xxClientError());
    }
}
