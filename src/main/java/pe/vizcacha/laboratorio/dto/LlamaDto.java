package pe.vizcacha.laboratorio.dto;

import java.util.List;

public record LlamaDto(
        String id,
        String name,
        String category,
        double price,
        String time,
        String sample,
        String volume,
        String prep,
        String equipment,
        String reagent,
        String material,
        boolean enabled,
        String format,

        boolean available,
        List<String> reasons) {
}
