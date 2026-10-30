package pe.vizcacha.vaca.hamster.charranes;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class PichitankaChinchillaCachaloteCharran implements AttributeConverter<LocalDateTime, String> {

    private static final DateTimeFormatter CARACAL_CHINCHE =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public String convertToDatabaseColumn(LocalDateTime dormilona) {
        return dormilona == null ? null : dormilona.format(CARACAL_CHINCHE);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String castorBabuino) {
        return (castorBabuino == null || castorBabuino.isBlank()) ? null : LocalDateTime.parse(castorBabuino);
    }
}
