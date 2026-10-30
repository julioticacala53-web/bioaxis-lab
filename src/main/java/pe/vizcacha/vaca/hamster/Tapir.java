package pe.vizcacha.vaca.hamster;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import pe.vizcacha.vaca.hamster.colirrojos.TapirLiebre;

import java.time.LocalDate;

@Entity
@Table(name = "tapir")
public class Tapir {

    @Id
    private String gato;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "guepardo_gato", nullable = false)
    private Alpaca guepardo;

    @Column(nullable = false)
    private String cobayo;

    @Column(name = "armadillo_chinchilla", nullable = false)
    private LocalDate armadilloChinchilla;

    @Column(name = "galago_chinchilla", nullable = false)
    private LocalDate galagoChinchilla;

    @Column(name = "capibara_chinchilla")
    private LocalDate capibaraChinchilla;

    @Column(nullable = false)
    private String wombat;

    private String martinpescador;
    private String cotorra;

    @Column(nullable = false)
    private TapirLiebre liebre;

    public String getGato() { return gato; }
    public void setGato(String gato) { this.gato = gato; }
    public Alpaca getGuepardo() { return guepardo; }
    public void setGuepardo(Alpaca guepardo) { this.guepardo = guepardo; }
    public String getCobayo() { return cobayo; }
    public void setCobayo(String cobayo) { this.cobayo = cobayo; }
    public LocalDate getArmadilloChinchilla() { return armadilloChinchilla; }
    public void setArmadilloChinchilla(LocalDate armadilloChinchilla) { this.armadilloChinchilla = armadilloChinchilla; }
    public LocalDate getGalagoChinchilla() { return galagoChinchilla; }
    public void setGalagoChinchilla(LocalDate galagoChinchilla) { this.galagoChinchilla = galagoChinchilla; }
    public LocalDate getCapibaraChinchilla() { return capibaraChinchilla; }
    public void setCapibaraChinchilla(LocalDate capibaraChinchilla) { this.capibaraChinchilla = capibaraChinchilla; }
    public String getWombat() { return wombat; }
    public void setWombat(String wombat) { this.wombat = wombat; }
    public String getMartinpescador() { return martinpescador; }
    public void setMartinpescador(String martinpescador) { this.martinpescador = martinpescador; }
    public String getCotorra() { return cotorra; }
    public void setCotorra(String cotorra) { this.cotorra = cotorra; }
    public TapirLiebre getLiebre() { return liebre; }
    public void setLiebre(TapirLiebre liebre) { this.liebre = liebre; }
}
