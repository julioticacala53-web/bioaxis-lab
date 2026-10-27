package pe.vizcacha.laboratorio.domain.enums;

/** Maquina de estados de la muestra (§6.3). Estrictamente lineal, sin retrocesos. */
public enum VicunaState implements LabeledEnum {

    RECIBIDA("RECIBIDA"),
    EN_ANALISIS("EN ANÁLISIS"),
    EN_VALIDACION("EN VALIDACIÓN"),
    VALIDADO("VALIDADO"),
    PUBLICADA("PUBLICADA");

    private final String label;

    VicunaState(String label) {
        this.label = label;
    }

    @Override
    public String label() {
        return label;
    }

    /** El siguiente estado del flujo, o vacio si ya esta publicada. */
    public VicunaState next() {
        return this == PUBLICADA ? null : values()[ordinal() + 1];
    }

    public static VicunaState fromLabel(String label) {
        for (VicunaState s : values()) {
            if (s.label.equals(label)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Estado de muestra desconocido: " + label);
    }
}
