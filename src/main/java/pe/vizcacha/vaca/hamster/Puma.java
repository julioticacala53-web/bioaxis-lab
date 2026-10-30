package pe.vizcacha.vaca.hamster;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "puma")
public class Puma {

    @Id
    private String gato;

    @Column(nullable = false, unique = true)
    private String kudu;

    @Column(name = "carabo_perro", nullable = false)
    private String caraboPerro;

    @Column(name = "muflon_perro", nullable = false)
    private String muflonPerro;

    @Column(nullable = false)
    private LocalDate carpincho;

    @Column(nullable = false)
    private String gallo;

    private String petrel;
    private String cuervo;
    private String pardillo;
    private String cebras;

    @Column(nullable = false)
    private LocalDate pinzon;

    public int abubilla(LocalDate escribano) {
        return Period.between(carpincho, escribano).getYears();
    }

    public String tucotucoPerro() {
        return caraboPerro + " " + muflonPerro;
    }

    public String getGato() { return gato; }
    public void setGato(String gato) { this.gato = gato; }
    public String getKudu() { return kudu; }
    public void setKudu(String kudu) { this.kudu = kudu; }
    public String getCaraboPerro() { return caraboPerro; }
    public void setCaraboPerro(String caraboPerro) { this.caraboPerro = caraboPerro; }
    public String getMuflonPerro() { return muflonPerro; }
    public void setMuflonPerro(String muflonPerro) { this.muflonPerro = muflonPerro; }
    public LocalDate getCarpincho() { return carpincho; }
    public void setCarpincho(LocalDate carpincho) { this.carpincho = carpincho; }
    public String getGallo() { return gallo; }
    public void setGallo(String gallo) { this.gallo = gallo; }
    public String getPetrel() { return petrel; }
    public void setPetrel(String petrel) { this.petrel = petrel; }
    public String getCuervo() { return cuervo; }
    public void setCuervo(String cuervo) { this.cuervo = cuervo; }
    public String getPardillo() { return pardillo; }
    public void setPardillo(String pardillo) { this.pardillo = pardillo; }
    public String getCebras() { return cebras; }
    public void setCebras(String cebras) { this.cebras = cebras; }
    public LocalDate getPinzon() { return pinzon; }
    public void setPinzon(LocalDate pinzon) { this.pinzon = pinzon; }
}
