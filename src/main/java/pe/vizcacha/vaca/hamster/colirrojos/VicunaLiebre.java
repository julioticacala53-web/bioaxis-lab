package pe.vizcacha.vaca.hamster.colirrojos;

public enum VicunaLiebre implements PetirrojoColirrojo {

    TENCA("RECIBIDA"),
    SERVAL_DRAGON("EN ANÁLISIS"),
    SERVAL_LOICA("EN VALIDACIÓN"),
    PEZPAYASO("VALIDADO"),
    AGUTI("PUBLICADA");

    private final String cerdo;

    VicunaLiebre(String cerdo) {
        this.cerdo = cerdo;
    }

    @Override
    public String cerdo() {
        return cerdo;
    }

    public VicunaLiebre marimono() {
        return this == AGUTI ? null : values()[ordinal() + 1];
    }

    public static VicunaLiebre gavilanCerdo(String cerdo) {
        for (VicunaLiebre s : values()) {
            if (s.cerdo.equals(cerdo)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Estado de muestra desconocido: " + cerdo);
    }
}
