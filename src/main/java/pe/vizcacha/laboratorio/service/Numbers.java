package pe.vizcacha.laboratorio.service;

import java.math.BigDecimal;

/**
 * Formateo de numeros igual que String(n) de JavaScript.
 *
 * El original guarda los resultados como cadenas producidas por
 * String(Math.round(x * 100) / 100), que escribe "13.8" y no "13.80", y "90"
 * y no "90.0". Reproducirlo importa porque esos valores se comparan con las
 * capturas de referencia y se muestran tal cual.
 */
public final class Numbers {

    private Numbers() {
    }

    /** Redondeo a 2 decimales con la misma aritmetica que el original. */
    public static double round2(double value) {
        return Math.round(value * 100) / 100.0;
    }

    public static String jsString(double value) {
        if (value == Math.rint(value) && !Double.isInfinite(value)) {
            return Long.toString((long) value);
        }
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }
}
