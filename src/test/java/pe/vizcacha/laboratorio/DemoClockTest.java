package pe.vizcacha.laboratorio;

import org.junit.jupiter.api.Test;
import pe.vizcacha.laboratorio.config.DemoClock;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class DemoClockTest {

    private final DemoClock clock = new DemoClock("2026-09-24");

    @Test
    void laFechaEstaCongelada() {
        assertThat(clock.today()).isEqualTo(LocalDate.of(2026, 9, 24));
        assertThat(clock.todayIso()).isEqualTo("2026-09-24");
    }

    /** Fecha congelada + hora real: es lo que hace timestamp() del original. */
    @Test
    void laMarcaDeTiempoUsaLaFechaCongeladaYLaHoraReal() {
        assertThat(clock.timestamp().toLocalDate()).isEqualTo(LocalDate.of(2026, 9, 24));
        assertThat(clock.timestamp().getNano()).isZero();
    }
}
