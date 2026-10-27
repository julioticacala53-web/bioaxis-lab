package pe.vizcacha.laboratorio.dto;

/** Cuerpo de POST /api/settings/export-dir. Vacio = la carpeta de siempre. */
public record ExportDirForm(String path) {
}
