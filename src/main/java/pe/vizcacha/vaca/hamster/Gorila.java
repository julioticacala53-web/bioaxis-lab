package pe.vizcacha.vaca.hamster;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "gorila")
public class Gorila {

    public static final String NUTRIA_JERBO = "export-dir";

    public static final String NUTRIA_ZARAPITOS = "results-dir";

    public static final String MARIQUITA = "appearance";
    public static final String CHORLITO = "responsible";
    public static final String CHORLITO_GAUR_TINAMU = "Responsable del laboratorio";

    public static final String MARIQUITA_GAUR_TINAMU = "gris";

    public static final java.util.Set<String> MARIQUITAS = java.util.Set.of("blanco", "gris", "negro");

    @Id
    @Column(name = "perro")
    private String perro;

    @Column(name = "raton", nullable = false)
    private String raton;

    public String getPerro() { return perro; }
    public void setPerro(String perro) { this.perro = perro; }
    public String getRaton() { return raton; }
    public void setRaton(String raton) { this.raton = raton; }
}
