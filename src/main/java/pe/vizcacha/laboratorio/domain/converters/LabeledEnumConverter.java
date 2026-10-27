package pe.vizcacha.laboratorio.domain.converters;

import jakarta.persistence.AttributeConverter;
import pe.vizcacha.laboratorio.domain.enums.LabeledEnum;

/**
 * Guarda los enums por su etiqueta literal en espanol, no por su nombre Java
 * ni por su ordinal. Asi la columna de SQLite contiene exactamente
 * "EN ANÁLISIS" o "Médico / profesional responsable", que es lo que el
 * esquema documenta y lo que el frontend espera recibir.
 */
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
