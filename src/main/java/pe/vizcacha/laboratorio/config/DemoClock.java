package pe.vizcacha.laboratorio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DemoClock {

    private static final String AUTOMATICO = "auto";

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

    public LocalDateTime timestamp() {
        return pinned != null
                ? LocalDateTime.of(pinned, LocalTime.now().withNano(0))
                : LocalDateTime.now().withNano(0);
    }
}
