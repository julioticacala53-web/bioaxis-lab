package pe.vizcacha.laboratorio.domain.enums;

public enum ResultFormat implements LabeledEnum {

    HEMOGRAM("hemogram"),
    GLUCOSE("glucose"),
    LIPID("lipid"),
    URINE("urine"),
    MICROBIOLOGY("microbiology"),
    TSH("tsh"),
    T4("t4");

    private final String label;

    ResultFormat(String label) {
        this.label = label;
    }

    @Override
    public String label() {
        return label;
    }

    public static ResultFormat fromLabel(String label) {
        for (ResultFormat f : values()) {
            if (f.label.equals(label)) {
                return f;
            }
        }
        throw new IllegalArgumentException("Formato de examen desconocido: " + label);
    }
}
