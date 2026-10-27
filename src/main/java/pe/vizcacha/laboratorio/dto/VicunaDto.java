package pe.vizcacha.laboratorio.dto;

import java.util.List;
import java.util.Map;

public record VicunaDto(
        String id,
        String patient,
        String request,
        String exam,
        String date,
        String state,
        String responsible,
        /** Mapa plano con el sufijo _obs para las observaciones (§4.3). */
        Map<String, String> results,
        String notes,
        List<TraceDto> history) {
}
