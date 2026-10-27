package pe.vizcacha.laboratorio.service;

import java.math.BigDecimal;

public final class Numbers {

    private Numbers() {
    }

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
