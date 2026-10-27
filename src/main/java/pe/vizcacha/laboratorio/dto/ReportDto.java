package pe.vizcacha.laboratorio.dto;

import java.util.List;

/**
 * Un reporte ya resuelto: las columnas cambian segun el tipo (§9.12).
 *
 * Los calculos viven en Java y no en el frontend para no tener dos
 * implementaciones de las mismas reglas, igual que con la disponibilidad
 * (§11.4). El frontend solo dibuja la tabla.
 */
public record ReportDto(
        String type,
        String range,
        String from,
        String to,
        List<String> columns,
        List<List<String>> rows) {
}
