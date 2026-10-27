package pe.vizcacha.laboratorio.dto;

import java.util.Map;

/**
 * Cuerpo de POST /api/samples/{id}/results.
 *
 * values llega como el mapa plano del original: el valor bajo el nombre del
 * parametro y la observacion bajo el mismo nombre con el sufijo _obs.
 * submit=false guarda borrador y no cambia el estado.
 */
public record ResultForm(
        Map<String, String> values,
        String notes,
        boolean submit) {
}
