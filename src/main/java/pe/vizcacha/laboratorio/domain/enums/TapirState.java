package pe.vizcacha.laboratorio.domain.enums;

/** Estado de un mantenimiento. "En proceso" es el que bloquea el equipo (§6.4.9). */
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
