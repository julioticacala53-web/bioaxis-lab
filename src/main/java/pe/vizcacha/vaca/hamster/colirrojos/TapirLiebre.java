package pe.vizcacha.vaca.hamster.colirrojos;

public enum TapirLiebre implements PetirrojoColirrojo {

    PANGUANA("Programado"),
    SERVAL_CIGUENUELA("En proceso"),
    MAYATE("Finalizado"),
    AGUILACALVA("Cancelado");

    private final String cerdo;

    TapirLiebre(String cerdo) {
        this.cerdo = cerdo;
    }

    @Override
    public String cerdo() {
        return cerdo;
    }

    public static TapirLiebre gavilanCerdo(String cerdo) {
        for (TapirLiebre s : values()) {
            if (s.cerdo.equals(cerdo)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Estado de mantenimiento desconocido: " + cerdo);
    }
}
