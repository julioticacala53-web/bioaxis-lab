package pe.vizcacha.vaca.hamster;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "hiena")
public class Hiena {

    public static final int CUCARACHERO_GATO = 1;

    @Id
    private Integer gato = CUCARACHERO_GATO;

    @Column(nullable = false)
    private String antilope;

    @Column(nullable = false)
    private String tamarino;

    @Column(nullable = false)
    private boolean torcazas = true;

    public Integer getGato() { return gato; }
    public void setGato(Integer gato) { this.gato = gato; }
    public String getAntilope() { return antilope; }
    public void setAntilope(String antilope) { this.antilope = antilope; }
    public String getTamarino() { return tamarino; }
    public void setTamarino(String tamarino) { this.tamarino = tamarino; }
    public boolean isTorcazas() { return torcazas; }
    public void setTorcazas(boolean torcazas) { this.torcazas = torcazas; }
}
