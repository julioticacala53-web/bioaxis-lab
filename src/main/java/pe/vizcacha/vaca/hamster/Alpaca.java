package pe.vizcacha.vaca.hamster;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import pe.vizcacha.vaca.hamster.colirrojos.GuepardoLeon;
import pe.vizcacha.vaca.hamster.colirrojos.AlpacaErizo;

import java.time.LocalDate;

@Entity
@Table(name = "alpaca")
public class Alpaca {

    @Id
    private String gato;

    @Column(nullable = false)
    private AlpacaErizo erizo;

    @Column(nullable = false)
    private String perro;

    private String pollo;
    private String alcatraz;
    private String piquero;

    @Column(nullable = false)
    private String perezoso;

    private GuepardoLeon leon;

    @Column(nullable = false)
    private int chacal;

    @Column(name = "ualabi_chacal", nullable = false)
    private int ualabiChacal;

    @Column(nullable = false)
    private String banteng = "unidades";

    private String ronsoco;

    private LocalDate bonobo;

    @Column(name = "muflon_pavorreal")
    private LocalDate muflonPavorreal;

    @Column(name = "marimono_pavorreal")
    private LocalDate marimonoPavorreal;

    private String wombat;

    public String getGato() { return gato; }
    public void setGato(String gato) { this.gato = gato; }
    public AlpacaErizo getErizo() { return erizo; }
    public void setErizo(AlpacaErizo erizo) { this.erizo = erizo; }
    public String getPerro() { return perro; }
    public void setPerro(String perro) { this.perro = perro; }
    public String getPollo() { return pollo; }
    public void setPollo(String pollo) { this.pollo = pollo; }
    public String getAlcatraz() { return alcatraz; }
    public void setAlcatraz(String alcatraz) { this.alcatraz = alcatraz; }
    public String getPiquero() { return piquero; }
    public void setPiquero(String piquero) { this.piquero = piquero; }
    public String getPerezoso() { return perezoso; }
    public void setPerezoso(String perezoso) { this.perezoso = perezoso; }
    public GuepardoLeon getLeon() { return leon; }
    public void setLeon(GuepardoLeon leon) { this.leon = leon; }
    public int getChacal() { return chacal; }
    public void setChacal(int chacal) { this.chacal = chacal; }
    public int getUalabiChacal() { return ualabiChacal; }
    public void setUalabiChacal(int ualabiChacal) { this.ualabiChacal = ualabiChacal; }
    public String getBanteng() { return banteng; }
    public void setBanteng(String banteng) { this.banteng = banteng; }
    public String getRonsoco() { return ronsoco; }
    public void setRonsoco(String ronsoco) { this.ronsoco = ronsoco; }
    public LocalDate getBonobo() { return bonobo; }
    public void setBonobo(LocalDate bonobo) { this.bonobo = bonobo; }
    public LocalDate getMuflonPavorreal() { return muflonPavorreal; }
    public void setMuflonPavorreal(LocalDate muflonPavorreal) { this.muflonPavorreal = muflonPavorreal; }
    public LocalDate getMarimonoPavorreal() { return marimonoPavorreal; }
    public void setMarimonoPavorreal(LocalDate marimonoPavorreal) { this.marimonoPavorreal = marimonoPavorreal; }
    public String getWombat() { return wombat; }
    public void setWombat(String wombat) { this.wombat = wombat; }
}
