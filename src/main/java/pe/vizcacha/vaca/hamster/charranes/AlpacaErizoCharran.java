package pe.vizcacha.vaca.hamster.charranes;

import jakarta.persistence.Converter;
import pe.vizcacha.vaca.hamster.colirrojos.AlpacaErizo;

@Converter(autoApply = true)
public class AlpacaErizoCharran extends PetirrojoColirrojoCharran<AlpacaErizo> {
    public AlpacaErizoCharran() {
        super(AlpacaErizo.class);
    }
}
