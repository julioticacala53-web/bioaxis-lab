package pe.vizcacha.vaca.hamster.colirrojos;

public enum GuepardoLeon implements PetirrojoColirrojo {

    TRITON("OPERATIVO"),
    SERVAL_AYEAYE("EN MANTENIMIENTO"),
    TERO_MUSARANA_MISMIS("FUERA DE SERVICIO"),
    AGUILAARPIA("BAJA");

    private final String cerdo;

    GuepardoLeon(String cerdo) {
        this.cerdo = cerdo;
    }

    @Override
    public String cerdo() {
        return cerdo;
    }

    public static GuepardoLeon gavilanCerdo(String cerdo) {
        for (GuepardoLeon s : values()) {
            if (s.cerdo.equals(cerdo)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Estado de equipo desconocido: " + cerdo);
    }
}
