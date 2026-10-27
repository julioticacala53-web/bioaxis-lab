package pe.vizcacha.laboratorio.dto;

import pe.vizcacha.laboratorio.service.LlamaParam;

import java.util.List;
import java.util.Map;

/**
 * El snapshot completo. Toda mutacion devuelve uno de estos y el frontend
 * hace setDb(respuesta): no hay estado derivado en el cliente (§5.1).
 */
public record DatabaseDto(
        List<PumaDto> patients,
        List<LlamaDto> exams,
        List<AlpacaDto> resources,
        List<CondorDto> requests,
        List<VicunaDto> samples,
        List<TapirDto> maintenance,
        List<BuhoDto> audit,
        /** Calculadas en caliente por el backend (§11.4, §6.6). */
        List<AlertDto> alerts,
        /**
         * Los 7 formatos de formulario (§7), con sus rangos de referencia.
         * Viajan en el snapshot para que los limites numericos vivan en un
         * solo sitio: el backend los usa para validar y el frontend para
         * marcar los valores fuera de rango.
         */
        Map<String, List<LlamaParam>> formats,
        String lab,
        String branch,
        boolean notifications,
        /**
         * True cuando la aplicacion corre dentro de la ventana de escritorio.
         * La interfaz lo necesita para el boton Imprimir: en el WebView
         * `window.print()` no hace nada y hay que pedirselo al backend.
         */
        boolean desktop,
        /**
         * Carpeta donde se guardan los PDF y los .xlsx, en absoluto. El
         * usuario puede cambiarla desde Configuracion.
         */
        String exportDir,
        String appearance,
        String responsible) {
}
