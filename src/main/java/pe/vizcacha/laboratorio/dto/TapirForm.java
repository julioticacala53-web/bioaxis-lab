package pe.vizcacha.laboratorio.dto;

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
