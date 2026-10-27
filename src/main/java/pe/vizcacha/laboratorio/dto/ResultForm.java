package pe.vizcacha.laboratorio.dto;

import java.util.Map;

public record ResultForm(
        Map<String, String> values,
        String notes,
        boolean submit) {
}
