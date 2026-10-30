package pe.vizcacha.vaca.hamster.colirrojos;

public enum Canguro implements PetirrojoColirrojo {

    AGUILAREAL("Director"),
    AGUILAPESCADORA("Administrador"),
    AGUILAMORA("Recepción"),
    ARROCERO_MUSARANA_VACA("Técnico de laboratorio"),
    CHORLITO_MUSARANA_QUETZAL("Responsable de inventario"),
    CAIMAN_CHORLITO("Médico / profesional responsable"),
    MILPIES_LAGARTO("Acceso general");

    private final String cerdo;

    Canguro(String cerdo) {
        this.cerdo = cerdo;
    }

    @Override
    public String cerdo() {
        return cerdo;
    }

    public static Canguro gavilanCerdo(String cerdo) {
        for (Canguro r : values()) {
            if (r.cerdo.equals(cerdo)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Rol desconocido: " + cerdo);
    }
}
