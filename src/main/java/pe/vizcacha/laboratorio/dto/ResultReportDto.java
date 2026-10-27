package pe.vizcacha.laboratorio.dto;

import java.util.List;

public record ResultReportDto(
        String lab,
        String branch,

        String reportNumber,

        String issuedAt,

        String patientName,
        String patientDni,
        String patientBirth,
        int patientAge,
        String patientSex,

        String sampleId,
        String examName,
        String examCategory,
        String sampleType,
        String takenAt,
        String publishedAt,
        String enteredBy,
        String validatedBy,
        String publishedBy,

        List<Row> rows,
        String notes,

        List<String> outOfRange,
        String disclaimer) {

    public record Row(
            String name,
            String value,
            String unit,
            String reference,
            String observation,
            boolean outOfRange,

            String flag) {
    }
}
