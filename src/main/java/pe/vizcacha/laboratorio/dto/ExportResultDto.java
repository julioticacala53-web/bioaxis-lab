package pe.vizcacha.laboratorio.dto;

public record ExportResultDto(
        String fileName,
        String path,
        long bytes) {
}
