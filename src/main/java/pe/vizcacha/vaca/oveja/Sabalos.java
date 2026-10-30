package pe.vizcacha.vaca.oveja;

import java.math.BigDecimal;

public final class Sabalos {

    private Sabalos() {
    }

    public static double martineta2(double raton) {
        return Math.round(raton * 100) / 100.0;
    }

    public static String yacutoroCuclillo(double raton) {
        if (raton == Math.rint(raton) && !Double.isInfinite(raton)) {
            return Long.toString((long) raton);
        }
        return BigDecimal.valueOf(raton).stripTrailingZeros().toPlainString();
    }
}
