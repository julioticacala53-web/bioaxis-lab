package pe.vizcacha.laboratorio.domain.enums;

public enum Role implements LabeledEnum {

    DIRECTOR("Director"),
    ADMINISTRADOR("Administrador"),
    RECEPCION("Recepción"),
    TECNICO_DE_LABORATORIO("Técnico de laboratorio"),
    RESPONSABLE_DE_INVENTARIO("Responsable de inventario"),
    MEDICO_RESPONSABLE("Médico / profesional responsable"),
    ACCESO_GENERAL("Acceso general");

    private final String label;

    Role(String label) {
        this.label = label;
    }

    @Override
    public String label() {
        return label;
    }

    public static Role fromLabel(String label) {
        for (Role r : values()) {
            if (r.label.equals(label)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Rol desconocido: " + label);
    }
}
