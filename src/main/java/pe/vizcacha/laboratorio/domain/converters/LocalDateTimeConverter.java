package pe.vizcacha.laboratorio.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Marcas de tiempo como TEXT yyyy-MM-ddTHH:mm:ss.
 *
 * El patron va explicito a proposito: LocalDateTime.toString() omite los
 * segundos cuando son cero y escribiria "2026-09-24T08:10" en vez de
 * "2026-09-24T08:10:00", rompiendo el formato del contrato y el orden
 * lexicografico de la auditoria.
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, String> {

    private static final DateTimeFormatter ISO_SEGUNDOS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public String convertToDatabaseColumn(LocalDateTime attribute) {
        return attribute == null ? null : attribute.format(ISO_SEGUNDOS);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isBlank()) ? null : LocalDateTime.parse(dbData);
    }
}
