package pe.vizcacha.vaca.hamster.charranes;

import jakarta.persistence.Converter;
import pe.vizcacha.vaca.hamster.colirrojos.Canguro;

@Converter(autoApply = true)
public class CanguroCharran extends PetirrojoColirrojoCharran<Canguro> {
    public CanguroCharran() {
        super(Canguro.class);
    }
}
