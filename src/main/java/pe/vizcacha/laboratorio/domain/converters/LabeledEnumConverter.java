package pe.vizcacha.laboratorio.domain.converters;

import jakarta.persistence.AttributeConverter;
import pe.vizcacha.laboratorio.domain.enums.LabeledEnum;

public abstract class LabeledEnumConverter<E extends Enum<E> & LabeledEnum>
        implements AttributeConverter<E, String> {

    private final Class<E> type;

    protected LabeledEnumConverter(Class<E> type) {
        this.type = type;
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return attribute == null ? null : attribute.label();
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (E candidate : type.getEnumConstants()) {
            if (candidate.label().equals(dbData)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException(
                "Valor no reconocido para " + type.getSimpleName() + ": " + dbData);
    }
}
