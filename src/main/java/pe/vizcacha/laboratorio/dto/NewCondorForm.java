package pe.vizcacha.laboratorio.dto;

import java.util.List;

/** Cuerpo de POST /api/requests. */
public record NewCondorForm(
        String patientId,
        List<String> examIds,
        boolean allowPending) {
}
