package pe.vizcacha.laboratorio.domain.enums;

/**
 * Los 7 formatos de formulario de resultados (§7). La etiqueta va en
 * minuscula porque el frontend la usa como clave para elegir la tabla de
 * parametros.
 */
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
