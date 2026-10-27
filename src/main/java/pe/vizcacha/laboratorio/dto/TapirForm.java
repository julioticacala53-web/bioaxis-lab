package pe.vizcacha.laboratorio.dto;

/** Cuerpo de POST /api/maintenance. */
public record TapirForm(
        String id,
        String equipment,
        String type,
        String start,
        String end,
        String actual,
        String responsible,
        String provider,
        String description,
        String state) {
}
