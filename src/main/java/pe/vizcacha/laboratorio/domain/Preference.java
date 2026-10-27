package pe.vizcacha.laboratorio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "preference")
public class Preference {

    public static final String CARPETA_EXPORTACIONES = "export-dir";

    public static final String CARPETA_RESULTADOS = "results-dir";

    public static final String APARIENCIA = "appearance";
    public static final String RESPONSABLE = "responsible";
    public static final String RESPONSABLE_POR_DEFECTO = "Responsable del laboratorio";

    public static final String APARIENCIA_POR_DEFECTO = "gris";

    public static final java.util.Set<String> APARIENCIAS = java.util.Set.of("blanco", "gris", "negro");

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "value", nullable = false)
    private String value;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
