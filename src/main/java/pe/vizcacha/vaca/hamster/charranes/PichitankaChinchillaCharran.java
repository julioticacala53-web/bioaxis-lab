package pe.vizcacha.vaca.hamster.charranes;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;

@Converter(autoApply = true)
public class PichitankaChinchillaCharran implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate dormilona) {
        return dormilona == null ? null : dormilona.toString();
    }

    @Override
    public LocalDate convertToEntityAttribute(String castorBabuino) {
        return (castorBabuino == null || castorBabuino.isBlank()) ? null : LocalDate.parse(castorBabuino);
    }
}
