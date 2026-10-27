package pe.vizcacha.laboratorio.dto;

/** Cuerpo de POST /api/resources/{id}/movements. direction es IN u OUT. */
public record MovementForm(
        String direction,
        Integer qty,
        String notes) {
}
