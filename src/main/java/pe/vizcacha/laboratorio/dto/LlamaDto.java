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
        /**
         * Calculados por el backend (§11.4). El original los recalculaba en
         * el render con availability(); aqui vienen resueltos para que la
         * regla viva en un solo sitio y no se desincronicen dos
         * implementaciones de lo mas importante del sistema.
         */
        boolean available,
        List<String> reasons) {
}
