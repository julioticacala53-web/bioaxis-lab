package pe.vizcacha.laboratorio.dto;

/**
 * Una alerta operativa del dashboard. El tono y el icono son los nombres que
 * usa el frontend tal cual (amber, red, blue · monitor, flask, box, calendar,
 * file, tube, book).
 */
public record AlertDto(
        String title,
        String detail,
        String module,
        String tone,
        String icon) {
}
