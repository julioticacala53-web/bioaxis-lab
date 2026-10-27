package pe.vizcacha.laboratorio.domain.enums;

/** Las 3 familias de recursos. Tambien es el nombre del modulo en auditoria. */
public enum AlpacaKind implements LabeledEnum {

    EQUIPOS("Equipos"),
    REACTIVOS("Reactivos"),
    MATERIALES("Materiales");

    private final String label;

    AlpacaKind(String label) {
        this.label = label;
    }

    @Override
    public String label() {
        return label;
    }

    public static AlpacaKind fromLabel(String label) {
        for (AlpacaKind k : values()) {
            if (k.label.equals(label)) {
                return k;
            }
        }
        throw new IllegalArgumentException("Tipo de recurso desconocido: " + label);
    }
}
