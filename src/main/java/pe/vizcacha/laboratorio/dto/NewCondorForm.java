package pe.vizcacha.laboratorio.dto;

import java.util.List;

public record NewCondorForm(
        String patientId,
        List<String> examIds,
        boolean allowPending) {
}
