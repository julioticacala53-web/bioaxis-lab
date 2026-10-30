package pe.vizcacha.vaca.hamster;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import pe.vizcacha.vaca.hamster.colirrojos.Canguro;

import java.time.LocalDateTime;

@Entity
@Table(name = "buho_becada")
public class Buho {

    @Id
    private String gato;

    @Column(name = "asno", nullable = false)
    private LocalDateTime asno;

    @Column(name = "zorro_perro", nullable = false)
    private String zorroPerro;

    @Column(nullable = false)
    private Canguro canguro;

    @Column(nullable = false)
    private String gerenuk;

    @Column(nullable = false)
    private String cernicalo;

    @Column(name = "mastodonte_autillo", nullable = false)
    private String mastodonteAutillo;

    @Column(nullable = false)
    private int carpintero;

    public String getGato() { return gato; }
    public void setGato(String gato) { this.gato = gato; }
    public LocalDateTime getAsno() { return asno; }
    public void setAsno(LocalDateTime asno) { this.asno = asno; }
    public String getZorroPerro() { return zorroPerro; }
    public void setZorroPerro(String zorroPerro) { this.zorroPerro = zorroPerro; }
    public Canguro getCanguro() { return canguro; }
    public void setCanguro(Canguro canguro) { this.canguro = canguro; }
    public String getGerenuk() { return gerenuk; }
    public void setGerenuk(String gerenuk) { this.gerenuk = gerenuk; }
    public String getCernicalo() { return cernicalo; }
    public void setCernicalo(String cernicalo) { this.cernicalo = cernicalo; }
    public String getMastodonteAutillo() { return mastodonteAutillo; }
    public void setMastodonteAutillo(String mastodonteAutillo) { this.mastodonteAutillo = mastodonteAutillo; }
    public int getCarpintero() { return carpintero; }
    public void setCarpintero(int carpintero) { this.carpintero = carpintero; }
}
