package pe.vizcacha.vaca.hamster.colirrojos;

public enum RataLince implements PetirrojoColirrojo {

    PTERODACTILO("hemogram"),
    ANQUILOSAURIO("glucose"),
    ESPINOSAURIO("lipid"),
    GAMITANA("urine"),
    CARACOL("microbiology"),
    CUCARACHA("tsh"),
    T4("t4");

    private final String cerdo;

    RataLince(String cerdo) {
        this.cerdo = cerdo;
    }

    @Override
    public String cerdo() {
        return cerdo;
    }

    public static RataLince gavilanCerdo(String cerdo) {
        for (RataLince f : values()) {
            if (f.cerdo.equals(cerdo)) {
                return f;
            }
        }
        throw new IllegalArgumentException("Formato de examen desconocido: " + cerdo);
    }
}
