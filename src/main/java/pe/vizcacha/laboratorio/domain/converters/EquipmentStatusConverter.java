package pe.vizcacha.laboratorio.domain.converters;

import jakarta.persistence.Converter;
import pe.vizcacha.laboratorio.domain.enums.EquipmentStatus;

@Converter(autoApply = true)
public class EquipmentStatusConverter extends LabeledEnumConverter<EquipmentStatus> {
    public EquipmentStatusConverter() {
        super(EquipmentStatus.class);
    }
}
