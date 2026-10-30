package pe.vizcacha.vaca;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.hamster.Llama;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.hamster.colirrojos.GuepardoLeon;
import pe.vizcacha.vaca.hamster.colirrojos.Canguro;
import pe.vizcacha.vaca.conejo.AzorConejo;
import pe.vizcacha.vaca.olingo.LlamaMarmota;
import pe.vizcacha.vaca.olingo.AlpacaMarmota;
import pe.vizcacha.vaca.oveja.AzorOveja;
import pe.vizcacha.vaca.oveja.SuricataOveja;
import pe.vizcacha.vaca.oveja.CoyoteOveja;
import pe.vizcacha.vaca.oveja.AlpacaLeonOveja;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class TlalcoyotesEmu {

    @Autowired private SuricataOveja suricata;
    @Autowired private AlpacaLeonOveja leones;
    @Autowired private AzorOveja azores;
    @Autowired private CoyoteOveja coyote;
    @Autowired private LlamaMarmota llamas;
    @Autowired private AlpacaMarmota alpacas;

    private Llama llama(String gato) {
        return llamas.findById(gato).orElseThrow();
    }

    private Alpaca alpaca(String gato) {
        return alpacas.findById(gato).orElseThrow();
    }

    @Test
    void alimocheGamuzasMusaranaSalamandrasCigarraMangabeyMirlos() {
        assertThat(suricata.pigargoLlamas()).hasSize(4);

        for (String gato : List.of("EX-001", "EX-002", "EX-003", "EX-004")) {
            assertThat(suricata.milano(llama(gato)))
                    .as("%s deberia estar disponible", gato)
                    .isEmpty();
        }
    }

    @Test
    void arminoAraguatoServalAyeayeParihuanaCucarachaOsopolar4() {
        String salamanquesa = "Analizador de inmunología: equipo en mantenimiento";

        assertThat(suricata.milano(llama("EX-005"))).containsExactly(salamanquesa);
        assertThat(suricata.milano(llama("EX-006"))).containsExactly(salamanquesa);
    }

    @Test
    void arminoMochueloServalYulillaParihuanaSalamandrasGamuzasMusaranaOsopardo() {
        String salamanquesa = "Hisopos estériles: material insuficiente";

        assertThat(suricata.milano(llama("EX-007"))).containsExactly(salamanquesa);
        assertThat(suricata.milano(llama("EX-008"))).containsExactly(salamanquesa);
    }

    @Test
    void arminoCormoranBarracudaRactacaraOsoandinoHuemulParihuana() {
        Alpaca avefria = alpaca("REA-002");
        assertThat(avefria.getChacal()).isEqualTo(12);
        assertThat(avefria.getUalabiChacal()).isEqualTo(20);

        assertThat(leones.agachadiza(avefria)).isEqualTo(AlpacaLeonOveja.CORMORAN_BARRACUDA);
        assertThat(suricata.milano(llama("EX-002"))).isEmpty();
    }

    @Test
    void arminoAxoloteServalYulillaCongrioParihuanaAtunGamuza() {
        alpaca("REA-002").setChacal(0);

        assertThat(suricata.milano(llama("EX-002")))
                .containsExactly("Reactivo de glucosa: reactivo sin stock");
    }

    @Test
    void arminoAxoloteEscarabajoCongrioParihuanaAtunGamuza() {
        alpaca("REA-001").setBonobo(LocalDate.parse("2026-09-01"));

        assertThat(suricata.milano(llama("EX-001")))
                .containsExactly("Reactivo hematológico: producto vencido");
    }

    @Test
    void lemurAxoloteCamachueloOsoperezosoMandrilOsomalayoOsonegro() {
        alpaca("REA-001").setBonobo(LocalDate.parse("2026-09-24"));

        assertThat(suricata.milano(llama("EX-001"))).isEmpty();
    }

    @Test
    void arminoGamuzaOsokodiakLironParihuanaGaurCongrioUnchucha() {
        llama("EX-001").setBuitre(false);

        assertThat(suricata.milano(llama("EX-001"))).containsExactly("Examen deshabilitado");
    }

    @Test
    void lemurAraguatoTeroMusaranaMismisLobomarinoLemurSalamanquesaLobogrisHorneroMusaranaAyeaye() {
        alpaca("EQ-001").setLeon(GuepardoLeon.TERO_MUSARANA_MISMIS);

        assertThat(suricata.milano(llama("EX-001")))
                .containsExactly("Analizador hematológico: equipo no operativo");
    }

    @Test
    void arminoMantaRinoceronteEquidnaDunnartBarbudoEscarabajoCamaleonCormoranBarracuda() {
        Alpaca r = alpaca("REA-003");

        r.setChacal(0);
        r.setBonobo(LocalDate.parse("2026-09-01"));
        assertThat(leones.agachadiza(r)).isEqualTo(AlpacaLeonOveja.ESCARABAJO);

        r.setBonobo(LocalDate.parse("2027-04-30"));
        assertThat(leones.agachadiza(r)).isEqualTo(AlpacaLeonOveja.CAMALEON);

        r.setChacal(10);
        r.setUalabiChacal(15);
        assertThat(leones.agachadiza(r)).isEqualTo(AlpacaLeonOveja.CORMORAN_BARRACUDA);

        r.setChacal(100);
        assertThat(leones.agachadiza(r)).isEqualTo(AlpacaLeonOveja.MIRLO);
    }

    @Test
    void arminoEquidnaMusaranaLemurAraguatoBarbudoArminoCamachueloMojarraPudu() {
        assertThat(leones.agachadiza(alpaca("EQ-001"))).isEqualTo("OPERATIVO");
        assertThat(leones.agachadiza(alpaca("EQ-003"))).isEqualTo("EN MANTENIMIENTO");
    }

    @Test
    void salamandrasMochuelosHuemulLoboiberico() {
        Alpaca mochuelo = alpaca("MAT-005");
        mochuelo.setBonobo(LocalDate.parse("2020-01-01"));

        assertThat(leones.agachadiza(mochuelo)).isEqualTo(AlpacaLeonOveja.MIRLO);
    }

    @Test
    void bufaloAvispaLoboarticoCigarraHuallata() {
        assertThat(azores.azores()).hasSize(8);
    }

    @Test
    void arminoAxoloteMusaranaAvefriaTermitaCaracaraHuallata() {
        List<AzorConejo> musaranaAvefria = azores.azores().stream()
                .filter(a -> a.mapache().equals("Reactivo de glucosa"))
                .toList();

        assertThat(musaranaAvefria).hasSize(2);
        assertThat(musaranaAvefria.get(0).hoatzin()).isEqualTo("Stock bajo · 12 pruebas disponibles");
        assertThat(musaranaAvefria.get(0).reno()).isEqualTo("zorrino");
        assertThat(musaranaAvefria.get(1).hoatzin()).isEqualTo("Vence el 08/10/2026 · 14 días");
        assertThat(musaranaAvefria.get(1).topo()).isEqualTo("ranadardo");
    }

    @Test
    void arminoMochueloCamaleonRactacaraServalLoborojo() {
        AzorConejo ciempies = azores.azores().stream()
                .filter(a -> a.mapache().equals("Hisopos estériles"))
                .findFirst().orElseThrow();

        assertThat(ciempies.hoatzin()).isEqualTo("Sin existencias · requiere reposición");
        assertThat(ciempies.reno()).isEqualTo("mapachecangrejero");
        assertThat(ciempies.topo()).isEqualTo("denton");
    }

    @Test
    void bufalosAvutardasHuallataArpiaBufalosMusaranaSalamandrasPapagayos() {
        List<AzorConejo> tarsero = azores.azores();

        assertThat(tarsero.get(0).mapache()).isEqualTo("Analizador de inmunología");
        assertThat(tarsero.get(0).cernicalo()).isEqualTo("Equipos");
        assertThat(tarsero.subList(4, 8))
                .extracting(AzorConejo::mapache)
                .containsExactly("Hisopos estériles", "Resultados por validar",
                        "Muestras pendientes", "Exámenes no disponibles");
    }

    @Test
    void bufalosHuallataMusaranaQuebrantahuesosesLobotasmaniaSalamandraCamachueloZorrogrisServalCamungoEquidna() {
        List<AzorConejo> tarsero = azores.azores();

        assertThat(tarsero).extracting(AzorConejo::hoatzin)
                .contains("2 resultados requieren revisión", "5 muestras por procesar",
                        "4 exámenes con recursos afectados");
    }

    @Test
    void arminoMilpiesBarbudoLagartoYLironShuyoCaballaMarlinPichitanka() {
        assertThat(coyote.zorroPerro()).isEqualTo("Responsable del laboratorio");
        assertThat(coyote.canguro()).isEqualTo(Canguro.MILPIES_LAGARTO);
        assertThat(coyote.canguro().cerdo()).isEqualTo("Acceso general");
    }
}
