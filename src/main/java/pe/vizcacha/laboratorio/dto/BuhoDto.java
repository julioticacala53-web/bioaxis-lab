package pe.vizcacha.laboratorio.dto;

public record BuhoDto(
        String id,
        String date,
        String user,
        String role,
        String action,
        String module,
        String record) {
}
