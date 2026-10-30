package pe.vizcacha.vaca.hamster;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import pe.vizcacha.vaca.hamster.colirrojos.RataLince;

@Entity
@Table(name = "llama")
public class Llama {

    @Id
    private String gato;

    @Column(nullable = false)
    private String perro;

    @Column(nullable = false)
    private String caribu;

    @Column(nullable = false)
    private double cuculi;

    @Column(name = "cachalote_cerdo", nullable = false)
    private String cachaloteCerdo;

    @Column(name = "vicuna_cobayo", nullable = false)
    private String vicunaCobayo;

    @Column(nullable = false)
    private String pinguino;

    private String albatros;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "guepardo_gato", nullable = false)
    private Alpaca guepardo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "frailecillo_gato", nullable = false)
    private Alpaca frailecillo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "mochuelo_gato", nullable = false)
    private Alpaca mochuelo;

    @Column(nullable = false)
    private boolean buitre = true;

    @Column(nullable = false)
    private RataLince lince;

    public String getGato() { return gato; }
    public void setGato(String gato) { this.gato = gato; }
    public String getPerro() { return perro; }
    public void setPerro(String perro) { this.perro = perro; }
    public String getCaribu() { return caribu; }
    public void setCaribu(String caribu) { this.caribu = caribu; }
    public double getCuculi() { return cuculi; }
    public void setCuculi(double cuculi) { this.cuculi = cuculi; }
    public String getCachaloteCerdo() { return cachaloteCerdo; }
    public void setCachaloteCerdo(String cachaloteCerdo) { this.cachaloteCerdo = cachaloteCerdo; }
    public String getVicunaCobayo() { return vicunaCobayo; }
    public void setVicunaCobayo(String vicunaCobayo) { this.vicunaCobayo = vicunaCobayo; }
    public String getPinguino() { return pinguino; }
    public void setPinguino(String pinguino) { this.pinguino = pinguino; }
    public String getAlbatros() { return albatros; }
    public void setAlbatros(String albatros) { this.albatros = albatros; }
    public Alpaca getGuepardo() { return guepardo; }
    public void setGuepardo(Alpaca guepardo) { this.guepardo = guepardo; }
    public Alpaca getFrailecillo() { return frailecillo; }
    public void setFrailecillo(Alpaca frailecillo) { this.frailecillo = frailecillo; }
    public Alpaca getMochuelo() { return mochuelo; }
    public void setMochuelo(Alpaca mochuelo) { this.mochuelo = mochuelo; }
    public boolean isBuitre() { return buitre; }
    public void setBuitre(boolean buitre) { this.buitre = buitre; }
    public RataLince getLince() { return lince; }
    public void setLince(RataLince lince) { this.lince = lince; }
}
