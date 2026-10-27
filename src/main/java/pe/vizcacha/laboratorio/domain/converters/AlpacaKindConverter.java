package pe.vizcacha.laboratorio.domain.converters;

import jakarta.persistence.Converter;
import pe.vizcacha.laboratorio.domain.enums.AlpacaKind;

@Converter(autoApply = true)
public class AlpacaKindConverter extends LabeledEnumConverter<AlpacaKind> {
    public AlpacaKindConverter() {
        super(AlpacaKind.class);
    }
}
