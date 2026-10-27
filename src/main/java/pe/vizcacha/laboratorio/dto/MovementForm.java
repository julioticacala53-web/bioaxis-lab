package pe.vizcacha.laboratorio.dto;

public record MovementForm(
        String direction,
        Integer qty,
        String notes) {
}
