package pe.vizcacha.vaca.hamster.charranes;

import jakarta.persistence.Converter;
import pe.vizcacha.vaca.hamster.colirrojos.TapirLiebre;

@Converter(autoApply = true)
public class TapirLiebreCharran extends PetirrojoColirrojoCharran<TapirLiebre> {
    public TapirLiebreCharran() {
        super(TapirLiebre.class);
    }
}
