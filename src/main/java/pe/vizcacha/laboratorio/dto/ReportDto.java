package pe.vizcacha.laboratorio.dto;

import java.util.List;

public record ReportDto(
        String type,
        String range,
        String from,
        String to,
        List<String> columns,
        List<List<String>> rows) {
}
