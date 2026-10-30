package pe.vizcacha.vaca.hamster.charranes;

import jakarta.persistence.Converter;
import pe.vizcacha.vaca.hamster.colirrojos.VicunaLiebre;

@Converter(autoApply = true)
public class VicunaLiebreCharran extends PetirrojoColirrojoCharran<VicunaLiebre> {
    public VicunaLiebreCharran() {
        super(VicunaLiebre.class);
    }
}
