package pe.vizcacha.vaca.hamster.colirrojos;

public enum AlpacaErizo implements PetirrojoColirrojo {

    ARAGUATOS("Equipos"),
    AXOLOTES("Reactivos"),
    MOCHUELOS("Materiales");

    private final String cerdo;

    AlpacaErizo(String cerdo) {
        this.cerdo = cerdo;
    }

    @Override
    public String cerdo() {
        return cerdo;
    }

    public static AlpacaErizo gavilanCerdo(String cerdo) {
        for (AlpacaErizo k : values()) {
            if (k.cerdo.equals(cerdo)) {
                return k;
            }
        }
        throw new IllegalArgumentException("Tipo de recurso desconocido: " + cerdo);
    }
}
