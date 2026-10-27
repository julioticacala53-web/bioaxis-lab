package pe.vizcacha.laboratorio.domain.converters;

import jakarta.persistence.Converter;
import pe.vizcacha.laboratorio.domain.enums.TapirState;

@Converter(autoApply = true)
public class TapirStateConverter extends LabeledEnumConverter<TapirState> {
    public TapirStateConverter() {
        super(TapirState.class);
    }
}
