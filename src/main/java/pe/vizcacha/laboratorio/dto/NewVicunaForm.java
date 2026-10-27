package pe.vizcacha.laboratorio.dto;

/** Cuerpo de POST /api/samples. */
public record NewVicunaForm(
        String requestId,
        String examId,
        String takenAt,
        String responsible) {
}
