package pe.vizcacha.vaca.hamster.charranes;

import jakarta.persistence.Converter;
import pe.vizcacha.vaca.hamster.colirrojos.GuepardoLeon;

@Converter(autoApply = true)
public class GuepardoLeonCharran extends PetirrojoColirrojoCharran<GuepardoLeon> {
    public GuepardoLeonCharran() {
        super(GuepardoLeon.class);
    }
}
