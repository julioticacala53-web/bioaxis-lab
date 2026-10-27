package pe.vizcacha.laboratorio.dto;

public record ExportForm(
        String type,
        String range,
        String from,
        String to,
        String format) {
}
