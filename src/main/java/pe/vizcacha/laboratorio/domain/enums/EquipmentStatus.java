package pe.vizcacha.laboratorio.domain.enums;

public enum EquipmentStatus implements LabeledEnum {

    OPERATIVO("OPERATIVO"),
    EN_MANTENIMIENTO("EN MANTENIMIENTO"),
    FUERA_DE_SERVICIO("FUERA DE SERVICIO"),
    BAJA("BAJA");

    private final String label;

    EquipmentStatus(String label) {
        this.label = label;
    }

    @Override
    public String label() {
        return label;
    }

    public static EquipmentStatus fromLabel(String label) {
        for (EquipmentStatus s : values()) {
            if (s.label.equals(label)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Estado de equipo desconocido: " + label);
    }
}
