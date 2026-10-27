package pe.vizcacha.laboratorio.domain.converters;

import jakarta.persistence.Converter;
import pe.vizcacha.laboratorio.domain.enums.VicunaState;

@Converter(autoApply = true)
public class VicunaStateConverter extends LabeledEnumConverter<VicunaState> {
    public VicunaStateConverter() {
        super(VicunaState.class);
    }
}
