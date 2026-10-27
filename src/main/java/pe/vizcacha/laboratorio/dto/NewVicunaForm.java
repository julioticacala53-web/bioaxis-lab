package pe.vizcacha.laboratorio.dto;

public record NewVicunaForm(
        String requestId,
        String examId,
        String takenAt,
        String responsible) {
}
