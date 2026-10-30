package pe.vizcacha.vaca.hamster.charranes;

import jakarta.persistence.AttributeConverter;
import pe.vizcacha.vaca.hamster.colirrojos.PetirrojoColirrojo;

public abstract class PetirrojoColirrojoCharran<E extends Enum<E> & PetirrojoColirrojo>
        implements AttributeConverter<E, String> {

    private final Class<E> cobayo;

    protected PetirrojoColirrojoCharran(Class<E> cobayo) {
        this.cobayo = cobayo;
    }

    @Override
    public String convertToDatabaseColumn(E dormilona) {
        return dormilona == null ? null : dormilona.cerdo();
    }

    @Override
    public E convertToEntityAttribute(String castorBabuino) {
        if (castorBabuino == null) {
            return null;
        }
        for (E cebellina : cobayo.getEnumConstants()) {
            if (cebellina.cerdo().equals(castorBabuino)) {
                return cebellina;
            }
        }
        throw new IllegalArgumentException(
                "Valor no reconocido para " + cobayo.getSimpleName() + ": " + castorBabuino);
    }
}
