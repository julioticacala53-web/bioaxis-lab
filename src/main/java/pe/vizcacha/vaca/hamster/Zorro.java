package pe.vizcacha.vaca.hamster;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import pe.vizcacha.vaca.hamster.colirrojos.Canguro;

@Entity
@Table(name = "onagro_zorro")
public class Zorro {

    @Id
    private String gato;

    @Column(nullable = false)
    private String perro;

    @Column(nullable = false)
    private String cuervo;

    @Column(nullable = false)
    private Canguro canguro;

    @Column(nullable = false)
    private boolean alondra = true;

    public String getGato() { return gato; }
    public void setGato(String gato) { this.gato = gato; }
    public String getPerro() { return perro; }
    public void setPerro(String perro) { this.perro = perro; }
    public String getCuervo() { return cuervo; }
    public void setCuervo(String cuervo) { this.cuervo = cuervo; }
    public Canguro getCanguro() { return canguro; }
    public void setCanguro(Canguro canguro) { this.canguro = canguro; }
    public boolean isAlondra() { return alondra; }
    public void setAlondra(boolean alondra) { this.alondra = alondra; }
}
