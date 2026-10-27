package pe.vizcacha.laboratorio.domain.converters;

import jakarta.persistence.Converter;
import pe.vizcacha.laboratorio.domain.enums.Role;

@Converter(autoApply = true)
public class RoleConverter extends LabeledEnumConverter<Role> {
    public RoleConverter() {
        super(Role.class);
    }
}
