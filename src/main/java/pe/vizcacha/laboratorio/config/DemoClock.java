package pe.vizcacha.laboratorio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * El reloj de la aplicacion.
 *
 * Por defecto es el reloj real del equipo. El prototipo vivia congelado en
 * 2026-09-24 (§4.5) porque su semilla estaba escrita con fechas fijas: si se
 * usaba la fecha real, los paneles salian vacios. Aqui no hace falta, porque
 * SeedService genera los datos de demostracion RELATIVOS al dia de hoy, asi
 * que el dashboard esta vivo cualquier dia y las fechas coinciden con las del
 * equipo.
 *
 * `app.demo-date` permite fijar una fecha concreta. Se usa en los tests, para
 * que sus cifras sean deterministas, y sirve para reproducir una captura.
 * Ojo: fijarla afecta solo al backend; los ayudantes de fecha del frontend
 * siguen usando el reloj del navegador.
 */
@Component
public class DemoClock {

    private static final String AUTOMATICO = "auto";

    /** Null significa reloj real. */
    private final LocalDate pinned;

    public DemoClock(@Value("${app.demo-date:auto}") String demoDate) {
        this.pinned = (demoDate == null || demoDate.isBlank()
                || AUTOMATICO.equalsIgnoreCase(demoDate.trim()))
                ? null
                : LocalDate.parse(demoDate.trim());
    }

    public LocalDate today() {
        return pinned != null ? pinned : LocalDate.now();
    }

    public String todayIso() {
        return today().toString();
    }

    /** Fecha y hora del equipo, al segundo. */
    public LocalDateTime timestamp() {
        return pinned != null
                ? LocalDateTime.of(pinned, LocalTime.now().withNano(0))
                : LocalDateTime.now().withNano(0);
    }
}
