package pe.vizcacha.laboratorio.dto;

import java.util.List;

public record CondorDto(
        String id,
        String patient,
        List<String> exams,
        String date,
        boolean pending) {
}
