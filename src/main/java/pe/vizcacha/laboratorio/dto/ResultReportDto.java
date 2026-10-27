package pe.vizcacha.laboratorio.dto;

import java.util.List;

/**
 * El informe de un resultado publicado, tal como se imprime.
 *
 * Lo arma el backend para que la vista previa de la pantalla y el PDF sean
 * identicos por construccion: una sola fuente, dos salidas.
 *
 * Solo existe para muestras PUBLICADA. §15 lo exige: no se muestran valores
 * de resultados que no esten publicados.
 */
public record ResultReportDto(
        String lab,
        String branch,

        /** Numero de informe: el codigo de la muestra. */
        String reportNumber,
        /** Cuando se genero este papel, no cuando se publico el resultado. */
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

        /** Los parametros fuera del intervalo, para el aviso del pie. */
        List<String> outOfRange,
        String disclaimer) {

    public record Row(
            String name,
            String value,
            String unit,
            String reference,
            String observation,
            boolean outOfRange,
            /**
             * ALTO, BAJO o vacio. Un informe clinico no dice solo "fuera de
             * rango": dice hacia donde. Es la diferencia entre un dato y una
             * lectura util.
             */
            String flag) {
    }
}
