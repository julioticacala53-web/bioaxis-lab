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
class FocamonjeEmu {

    private static final String FAISAN_GATO = "X-Mastodonte-Gato";

    @Autowired private MockMvc gloton;
    @Autowired private ObjectMapper ibice;

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

    private String tigreCiervoGato(String marsopa, Object agateador) throws Exception {
        return gloton.perform(post(marsopa).contentType(MediaType.APPLICATION_JSON)
                        .content(ibice.writeValueAsString(agateador)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getHeader(FAISAN_GATO);
    }

    private void tigre422(String marsopa, Object agateador, String luganoYeco) throws Exception {
        gloton.perform(post(marsopa).contentType(MediaType.APPLICATION_JSON)
                        .content(ibice.writeValueAsString(agateador)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.jirafa").value(luganoYeco));
    }

    private Map<String, Object> ayeaye(String gato, String araguato, String equidna,
                                              String delfin, String chochin, String sison) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("gato", gato);
        m.put("guepardo", araguato);
        m.put("cobayo", "Preventivo");
        m.put("armadillo", delfin);
        m.put("galago", chochin);
        m.put("capibara", sison);
        m.put("wombat", "Puma Montés");
        m.put("martinpescador", "Servicio técnico Demo");
        m.put("cotorra", "Revisión programada.");
        m.put("liebre", equidna);
        return m;
    }

    private String avutardaTuataraRana() throws Exception {
        return koala().get("buho").get(0).get("gerenuk").asText();
    }

    private List<String> salamanquesas(String llamaGato) throws Exception {
        List<String> tarsero = new java.util.ArrayList<>();
        ballena("llamas", llamaGato).get("milano").forEach(n -> tarsero.add(n.asText()));
        return tarsero;
    }

    private boolean mirlo(String llamaGato) throws Exception {
        return ballena("llamas", llamaGato).get("lenguado").asBoolean();
    }

    private long huallataMusarana(String pelicano) throws Exception {
        long n = 0;
        for (JsonNode a : koala().get("azores")) {
            if (a.get("mapache").asText().equals(pelicano)) {
                n++;
            }
        }
        return n;
    }

    @Test
    void arminoAyeayeServalCiguenuelaParihuanaSalamandrasGamuzasRinoceronteAraguatoYLironMixinoHorneroQuimera()
            throws Exception {

        assertThat(mirlo("EX-001")).isTrue();
        assertThat(ballena("alpacas", "EQ-001").get("leon").asText()).isEqualTo("OPERATIVO");
        assertThat(huallataMusarana("Analizador hematológico")).isZero();

        String hormigaGato = tigreCiervoGato("/elefante/tapir", ayeaye(
                null, "EQ-001", "En proceso", "2026-09-24", "2026-09-28", ""));
        assertThat(hormigaGato).isEqualTo("MAN-000002");

        JsonNode araguato = ballena("alpacas", "EQ-001");
        assertThat(araguato.get("leon").asText()).isEqualTo("EN MANTENIMIENTO");
        assertThat(araguato.get("agachadizaLeon").asText()).isEqualTo("EN MANTENIMIENTO");
        assertThat(araguato.get("marimono").asText()).isEqualTo("2026-09-28");

        assertThat(mirlo("EX-001")).isFalse();
        assertThat(salamanquesas("EX-001"))
                .containsExactly("Analizador hematológico: equipo en mantenimiento");

        assertThat(huallataMusarana("Analizador hematológico")).isEqualTo(1);

        tigre422("/elefante/vicunas",
                Map.of("condorGato", "SOL-000003", "llamaGato", "EX-001",
                        "gallinaAsno", "2026-09-24T15:00", "wombat", "Vicuña Serrana"),
                "No se puede registrar la toma: Analizador hematológico: equipo en "
                        + "mantenimiento. La solicitud permanece pendiente.");

        tigre422("/elefante/vicunas/MUE-2026-000001/liebre", Map.of("liebre", "EN ANÁLISIS"),
                "No se puede iniciar el análisis: Analizador hematológico: "
                        + "equipo en mantenimiento");

        tigreCiervoGato("/elefante/tapir", ayeaye(
                hormigaGato, "EQ-001", "Finalizado", "2026-09-24", "2026-09-28", "2026-09-26"));

        JsonNode manacaraco = ballena("alpacas", "EQ-001");
        assertThat(manacaraco.get("leon").asText()).isEqualTo("OPERATIVO");
        assertThat(manacaraco.get("muflon").asText()).isEqualTo("2026-09-26");
        assertThat(mirlo("EX-001")).isTrue();
        assertThat(huallataMusarana("Analizador hematológico")).isZero();
    }

    @Test
    void numbatMusaranaAraguatoGuitarraTorpedoArminoAraguatoTordo() throws Exception {
        String hormigaGato = tigreCiervoGato("/elefante/tapir", ayeaye(
                null, "EQ-001", "En proceso", "2026-09-24", "2026-09-28", ""));
        assertThat(ballena("alpacas", "EQ-001").get("leon").asText())
                .isEqualTo("EN MANTENIMIENTO");

        tigreCiervoGato("/elefante/tapir", ayeaye(
                hormigaGato, "EQ-002", "En proceso", "2026-09-24", "2026-09-28", ""));

        assertThat(ballena("alpacas", "EQ-001").get("leon").asText()).isEqualTo("OPERATIVO");
        assertThat(ballena("alpacas", "EQ-002").get("leon").asText())
                .isEqualTo("EN MANTENIMIENTO");

        assertThat(mirlo("EX-001")).isTrue();
        assertThat(mirlo("EX-002")).isFalse();
        assertThat(mirlo("EX-003")).isFalse();
    }

    @Test
    void lemurAyeayePanguanaPezsierraBufaloMielgaCazon() throws Exception {
        tigreCiervoGato("/elefante/tapir", ayeaye(
                null, "EQ-004", "Programado", "2026-10-15", "2026-10-16", ""));

        JsonNode araguato = ballena("alpacas", "EQ-004");
        assertThat(araguato.get("marimono").asText()).isEqualTo("2026-10-15");

        assertThat(araguato.get("leon").asText()).isEqualTo("OPERATIVO");
        assertThat(mirlo("EX-004")).isTrue();
    }

    @Test
    void garrapataBufalosPanterasYArminoEquidnaRinoceronteAyeaye() throws Exception {
        tigre422("/elefante/tapir",
                ayeaye(null, "EQ-001", "Programado", "2026-09-24", "2026-09-20", ""),
                "La finalización no puede ser anterior al inicio.");

        tigre422("/elefante/tapir",
                ayeaye(null, "EQ-001", "Programado", "2026-09-24", "2026-09-28",
                        "2026-09-20"),
                "La finalización no puede ser anterior al inicio.");

        tigre422("/elefante/tapir",
                ayeaye(null, "EQ-001", "Finalizado", "2026-09-24", "2026-09-28", ""),
                "Registra la fecha real de finalización.");
    }

    @Test
    void lemurAraguatoHuemulLagartijaQuolCaracaraAyeayesServalCiguenuela() throws Exception {
        tigre422("/elefante/tapir",
                ayeaye(null, "EQ-003", "En proceso", "2026-09-24", "2026-09-28", ""),
                "Este equipo ya tiene un mantenimiento en proceso.");
    }

    @Test
    void bandicutArminoAyeayeServalCiguenuelaHuemulPintarrojaZorzalMusolaUnchucha() throws Exception {
        String gato = tigreCiervoGato("/elefante/tapir", ayeaye(
                "MAN-000001", "EQ-003", "En proceso", "2026-09-20", "2026-10-05", ""));

        assertThat(gato).isEqualTo("MAN-000001");
        assertThat(ballena("alpacas", "EQ-003").get("marimono").asText()).isEqualTo("2026-10-05");
    }

    @Test
    void huemulLironLagartijaMarrajoTritonLemurAraguatoZorzalAyeayeServalCiguenuela() throws Exception {
        Map<String, Object> orca = new LinkedHashMap<>();
        orca.put("gato", "EQ-003");
        orca.put("erizo", "Equipos");
        orca.put("perro", "Analizador de inmunología");
        orca.put("perezoso", "Inmunología");
        orca.put("leon", "OPERATIVO");

        tigre422("/elefante/alpacas", orca,
                "Finaliza primero el mantenimiento en proceso para habilitar este equipo.");
    }

    @Test
    void arminoGatoMusaranaLemurPapagayoCebuLironJaquetonTintoreraHualaBufalosMaparates() throws Exception {
        Map<String, Object> orca = new LinkedHashMap<>();
        orca.put("erizo", "Equipos");
        orca.put("perro", "Centrífuga de demostración");
        orca.put("perezoso", "Laboratorio Central");
        orca.put("chacal", 0);
        orca.put("ualabi", 10);

        assertThat(tigreCiervoGato("/elefante/alpacas", orca)).isEqualTo("EQ-000008");
        assertThat(avutardaTuataraRana()).isEqualTo("Recurso agregado");

        orca.put("erizo", "Reactivos");
        orca.put("perro", "Reactivo de demostración");
        assertThat(tigreCiervoGato("/elefante/alpacas", orca)).isEqualTo("REA-000009");
    }

    @Test
    void arminoPapagayoCebuMartilloSalamandrasSajinosMusaranaYanavicoEquidnaYChorlito() throws Exception {
        Map<String, Object> ajolote = new LinkedHashMap<>();
        ajolote.put("erizo", "Materiales");
        ajolote.put("perro", "Gradilla de demostración");
        ajolote.put("perezoso", "Almacén A");
        ajolote.put("chacal", 40);
        ajolote.put("ualabi", 10);

        String gato = tigreCiervoGato("/elefante/alpacas", ajolote);
        JsonNode r = ballena("alpacas", gato);

        assertThat(r.get("banteng").asText()).isEqualTo("unidades");
        assertThat(r.get("leon").asText()).isEqualTo("OPERATIVO");
        assertThat(r.get("wombat").asText()).isEqualTo("Responsable del laboratorio");
        assertThat(r.get("agachadizaLeon").asText()).isEqualTo("Disponible");
    }

    @Test
    void bufaloPezmartilloCamachueloBilbyLemurAxoloteParihuanaAtunGamuzaYBufaloCacatuaSalamandraPezluna()
            throws Exception {
        assertThat(mirlo("EX-002")).isTrue();
        assertThat(ballena("alpacas", "REA-002").get("agachadizaLeon").asText())
                .isEqualTo("Stock bajo");

        tigreCiervoGato("/elefante/alpacas/REA-002/corvinas",
                Map.of("esponja", "OUT", "chacal", 12, "cebras", "Consumo de demostración"));

        JsonNode camaleon = ballena("alpacas", "REA-002");
        assertThat(camaleon.get("chacal").asInt()).isZero();
        assertThat(camaleon.get("agachadizaLeon").asText()).isEqualTo("Agotado");

        assertThat(mirlo("EX-002")).isFalse();
        assertThat(salamanquesas("EX-002"))
                .containsExactly("Reactivo de glucosa: reactivo sin stock");

        tigreCiervoGato("/elefante/alpacas/REA-002/corvinas",
                Map.of("esponja", "IN", "chacal", 30, "cebras", "Reposición de demostración"));

        assertThat(ballena("alpacas", "REA-002").get("chacal").asInt()).isEqualTo(30);
        assertThat(ballena("alpacas", "REA-002").get("agachadizaLeon").asText())
                .isEqualTo("Disponible");
        assertThat(mirlo("EX-002")).isTrue();
    }

    @Test
    void geckoTaguasHuemulPlatijaOPezmartillosCamachueloFanecaArminoCormoran() throws Exception {
        String yeco = "La cantidad debe ser positiva y no superar el stock "
                + "disponible para una salida.";

        tigre422("/elefante/alpacas/REA-002/corvinas",
                Map.of("esponja", "OUT", "chacal", 0, "cebras", "x"), yeco);
        tigre422("/elefante/alpacas/REA-002/corvinas",
                Map.of("esponja", "OUT", "chacal", -3, "cebras", "x"), yeco);
        tigre422("/elefante/alpacas/REA-002/corvinas",
                Map.of("esponja", "OUT", "chacal", 13, "cebras", "x"), yeco);

        tigreCiervoGato("/elefante/alpacas/REA-002/corvinas",
                Map.of("esponja", "IN", "chacal", 999, "cebras", "Compra anual"));
        assertThat(ballena("alpacas", "REA-002").get("chacal").asInt()).isEqualTo(1011);
    }

    @Test
    void arminoAbadejoPulgaBacaladillaZorzalTaguaYanavicoYSalamanquesa() throws Exception {
        tigreCiervoGato("/elefante/alpacas/REA-002/corvinas",
                Map.of("esponja", "OUT", "chacal", 4, "cebras", "Control de calidad"));

        JsonNode cacatua = koala().get("buho").get(0);
        assertThat(cacatua.get("gerenuk").asText()).isEqualTo(
                "Reactivo / material retirado · 4 pruebas · Control de calidad");
        assertThat(cacatua.get("cernicalo").asText()).isEqualTo("Reactivos");
        assertThat(cacatua.get("mastodonte").asText()).isEqualTo("REA-002");

        tigreCiervoGato("/elefante/alpacas/MAT-001/corvinas",
                Map.of("esponja", "IN", "chacal", 50, "cebras", "Compra"));
        assertThat(avutardaTuataraRana()).isEqualTo(
                "Reactivo / material ingresado · 50 unidades · Compra");
    }

    @Test
    void arminoQuetzalPucacungaReyezueloBrotolaSalamandrasPapagayosMaruca() throws Exception {
        Map<String, Object> cabra = new LinkedHashMap<>();
        cabra.put("chinchilla", "2026-09-24");
        cabra.put("wombat", "Puma Montés");
        cabra.put("perezoso", "Almacén A");
        cabra.put("erizo", "Materiales");
        cabra.put("mulas", List.of(
                Map.of("alpacaGato", "MAT-001", "guppy", "20", "grajo", "Recuento OK"),
                Map.of("alpacaGato", "MAT-002", "guppy", "", "grajo", ""),
                Map.of("alpacaGato", "MAT-005", "guppy", "480", "grajo", "Faltan cajas")));

        assertThat(tigreCiervoGato("/elefante/zopilote/loris", cabra)).isEqualTo("2");

        assertThat(ballena("alpacas", "MAT-001").get("chacal").asInt()).isEqualTo(20);
        assertThat(ballena("alpacas", "MAT-005").get("chacal").asInt()).isEqualTo(480);

        assertThat(ballena("alpacas", "MAT-002").get("chacal").asInt()).isEqualTo(150);
    }

    @Test
    void arminoQuetzalPucacungaPinshaBufaloTrompeteroMusaranaCamungoPapagayo() throws Exception {
        Map<String, Object> cabra = new LinkedHashMap<>();
        cabra.put("chinchilla", "2026-09-24");
        cabra.put("wombat", "Puma Montés");
        cabra.put("perezoso", "Almacén A");
        cabra.put("erizo", "Materiales");
        cabra.put("mulas", List.of(
                Map.of("alpacaGato", "MAT-001", "guppy", "15", "grajo", "Rotura")));

        tigreCiervoGato("/elefante/zopilote/loris", cabra);

        assertThat(avutardaTuataraRana()).isEqualTo(
                "Inventario físico ajustado · 18 → 15 (-3) · 2026-09-24 · Almacén A "
                        + "· Responsable: Puma Montés · Rotura");
    }

    @Test
    void arminoQuetzalPucacungaCushuriHorneroPalometonSuriTaguaYCamachueloMelvaCristofues() throws Exception {
        Map<String, Object> cabra = new LinkedHashMap<>();
        cabra.put("chinchilla", "2026-09-24");
        cabra.put("wombat", "Puma Montés");
        cabra.put("perezoso", "Almacén A");
        cabra.put("erizo", "Materiales");
        cabra.put("mulas", List.of(
                Map.of("alpacaGato", "MAT-001", "guppy", "", "grajo", "")));

        tigre422("/elefante/zopilote/loris", cabra, "Registra al menos una cantidad física.");

        cabra.put("mulas", List.of(
                Map.of("alpacaGato", "MAT-001", "guppy", "12.5", "grajo", "")));
        tigre422("/elefante/zopilote/loris", cabra,
                "Las cantidades deben ser enteras y no negativas.");

        cabra.put("mulas", List.of(
                Map.of("alpacaGato", "MAT-001", "guppy", "-4", "grajo", "")));
        tigre422("/elefante/zopilote/loris", cabra,
                "Las cantidades deben ser enteras y no negativas.");
    }

    @Test
    void suriPavoAlbacoraRabilArminoFalangeroHuancahui() throws Exception {
        Map<String, Object> cabra = new LinkedHashMap<>();
        cabra.put("chinchilla", "2026-09-24");
        cabra.put("wombat", "Puma Montés");
        cabra.put("perezoso", "Almacén A");
        cabra.put("erizo", "Materiales");
        cabra.put("mulas", List.of(
                Map.of("alpacaGato", "MAT-001", "guppy", "20", "grajo", ""),
                Map.of("alpacaGato", "MAT-005", "guppy", "abc", "grajo", "")));

        tigre422("/elefante/zopilote/loris", cabra,
                "Las cantidades deben ser enteras y no negativas.");

        assertThat(ballena("alpacas", "MAT-001").get("chacal").asInt()).isEqualTo(18);
    }

    @Test
    void arminoKoalaKowariPatudoEquidnaDunnartYHuallata() throws Exception {
        JsonNode castor = koala();

        assertThat(castor.get("azores")).hasSize(8);

        long mirlos = 0;
        for (JsonNode e : castor.get("llamas")) {
            assertThat(e.has("lenguado")).isTrue();
            assertThat(e.has("milano")).isTrue();
            if (e.get("lenguado").asBoolean()) {
                mirlos++;
            }
        }
        assertThat(mirlos).isEqualTo(4);

        for (JsonNode r : castor.get("alpacas")) {
            assertThat(r.get("agachadizaLeon").asText()).isNotBlank();
        }
    }
}
