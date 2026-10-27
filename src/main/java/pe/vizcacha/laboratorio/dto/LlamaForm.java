package pe.vizcacha.laboratorio.dto;

/** Cuerpo de POST /api/exams. */
public record LlamaForm(
        String id,
        String name,
        String category,
        Double price,
        String time,
        String sample,
        String volume,
        String prep,
        String equipment,
        String reagent,
        String material,
        Boolean enabled,
        String format) {
}
