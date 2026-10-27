package pe.vizcacha.laboratorio.domain.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;

/**
 * Fechas como TEXT ISO yyyy-MM-dd. No es cosmetico: la regla de vencimiento
 * de reactivos (§6.1) compara las fechas como cadenas, y eso solo funciona
 * si el formato almacenado es exactamente este.
 */
@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isBlank()) ? null : LocalDate.parse(dbData);
    }
}
