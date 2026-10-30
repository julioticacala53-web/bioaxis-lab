package pe.vizcacha.vaca.hamster.charranes;

import jakarta.persistence.Converter;
import pe.vizcacha.vaca.hamster.colirrojos.RataLince;

@Converter(autoApply = true)
public class RataLinceCharran extends PetirrojoColirrojoCharran<RataLince> {
    public RataLinceCharran() {
        super(RataLince.class);
    }
}
