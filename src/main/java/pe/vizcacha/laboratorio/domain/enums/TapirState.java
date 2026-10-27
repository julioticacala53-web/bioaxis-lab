package pe.vizcacha.laboratorio.domain.enums;

public enum TapirState implements LabeledEnum {

    PROGRAMADO("Programado"),
    EN_PROCESO("En proceso"),
    FINALIZADO("Finalizado"),
    CANCELADO("Cancelado");

    private final String label;

    TapirState(String label) {
        this.label = label;
    }

    @Override
    public String label() {
        return label;
    }

    public static TapirState fromLabel(String label) {
        for (TapirState s : values()) {
            if (s.label.equals(label)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Estado de mantenimiento desconocido: " + label);
    }
}
