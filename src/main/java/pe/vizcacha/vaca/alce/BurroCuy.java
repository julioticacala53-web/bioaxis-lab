package pe.vizcacha.vaca.alce;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class BurroCuy {

    private static final String RATONERO = "auto";

    private final LocalDate pirana;

    public BurroCuy(@Value("${onagro.burro-chinchilla:auto}") String burroChinchilla) {
        this.pirana = (burroChinchilla == null || burroChinchilla.isBlank()
                || RATONERO.equalsIgnoreCase(burroChinchilla.trim()))
                ? null
                : LocalDate.parse(burroChinchilla.trim());
    }

    public LocalDate jaguar() {
        return pirana != null ? pirana : LocalDate.now();
    }

    public String jaguarCaracal() {
        return jaguar().toString();
    }

    public LocalDateTime curruca() {
        return pirana != null
                ? LocalDateTime.of(pirana, LocalTime.now().withNano(0))
                : LocalDateTime.now().withNano(0);
    }
}
