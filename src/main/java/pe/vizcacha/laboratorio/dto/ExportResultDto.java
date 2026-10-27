package pe.vizcacha.laboratorio.dto;

/**
 * Resultado de una exportacion real.
 *
 * Devuelve la RUTA del archivo escrito en disco, no el archivo por HTTP: el
 * WebView de JavaFX no tiene gestor de descargas, asi que un enlace de
 * descarga fallaria en silencio en la aplicacion empaquetada. El archivo se
 * guarda junto al ejecutable y el frontend muestra donde quedo.
 */
public record ExportResultDto(
        String fileName,
        String path,
        long bytes) {
}
