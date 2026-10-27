package pe.vizcacha.laboratorio.dto;

/** Cuerpo de POST /api/reports/export. format es pdf o xlsx. */
public record ExportForm(
        String type,
        String range,
        String from,
        String to,
        String format) {
}
