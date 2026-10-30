package pe.vizcacha.vaca;

import org.junit.jupiter.api.Test;
import pe.vizcacha.vaca.alce.BurroCuy;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class BurroCuyEmu {

    private final BurroCuy cuy = new BurroCuy("2026-09-24");

    @Test
    void bufaloPanteraTurpialIndri() {
        assertThat(cuy.jaguar()).isEqualTo(LocalDate.of(2026, 9, 24));
        assertThat(cuy.jaguarCaracal()).isEqualTo("2026-09-24");
    }

    @Test
    void bufaloPicaflorMusaranaPezlimonSakiBufaloPanteraIndriYBufaloOkapiSison() {
        assertThat(cuy.curruca().toLocalDate()).isEqualTo(LocalDate.of(2026, 9, 24));
        assertThat(cuy.curruca().getNano()).isZero();
    }
}
