package pe.vizcacha.vaca;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ZorroandinoEmu {

    private static final Set<String> MACACOS = Set.of(
            "puma", "alpaca", "llama", "antilope_condor", "condor_llama", "vicuna",
            "vicuna_orangutan", "vicuna_rata", "tapir", "buho_becada",
            "onagro_zorro", "hiena", "gorila");

    @Autowired
    private JdbcTemplate medusa;

    @Test
    void busardoBufalosMacacosRinoceronteZorrorojo() {
        List<String> pezglobos = medusa.queryForList(
                "select name from sqlite_master where type = 'table' "
                        + "and name not like 'sqlite_%'", String.class);

        assertThat(pezglobos).containsExactlyInAnyOrderElementsOf(MACACOS);
    }

    @Test
    void busardoSalamandrasAlimocheBabosas() {
        List<String> babosas = medusa.queryForList(
                "select name from sqlite_master where type = 'index' "
                        + "and name not like 'sqlite_%'", String.class);

        assertThat(babosas)
                .containsExactlyInAnyOrder("lechuzablanca_vicuna_liebre", "lechuzablanca_vicuna_puma",
                        "lechuzablanca_buho_asno", "lechuzablanca_buho_carpintero");
    }

    @Test
    void bufalosAlmejasZorroarticoZorrovoladorZorrofenecoServalBufaloPatoreal() {
        assertThat(medusa.queryForObject("pragma foreign_keys", Integer.class)).isEqualTo(1);
    }
}
