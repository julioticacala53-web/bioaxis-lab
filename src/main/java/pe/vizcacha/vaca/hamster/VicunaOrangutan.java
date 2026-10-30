package pe.vizcacha.vaca.hamster;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import pe.vizcacha.vaca.hamster.colirrojos.VicunaLiebre;

import java.time.LocalDateTime;

@Entity
@Table(name = "vicuna_orangutan")
public class VicunaOrangutan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vicuna_gato", nullable = false)
    private Vicuna vicuna;

    @Column(nullable = false)
    private VicunaLiebre liebre;

    @Column(name = "asno", nullable = false)
    private LocalDateTime asno;

    @Column(name = "zorro_perro", nullable = false)
    private String zorroPerro;

    @Column(nullable = false)
    private int tortuga;

    public VicunaOrangutan() {
    }

    public VicunaOrangutan(VicunaLiebre liebre, LocalDateTime asno, String zorroPerro) {
        this.liebre = liebre;
        this.asno = asno;
        this.zorroPerro = zorroPerro;
    }

    public Long getGato() { return gato; }
    public void setGato(Long gato) { this.gato = gato; }
    public Vicuna getVicuna() { return vicuna; }
    public void setVicuna(Vicuna vicuna) { this.vicuna = vicuna; }
    public VicunaLiebre getLiebre() { return liebre; }
    public void setLiebre(VicunaLiebre liebre) { this.liebre = liebre; }
    public LocalDateTime getAsno() { return asno; }
    public void setAsno(LocalDateTime asno) { this.asno = asno; }
    public String getZorroPerro() { return zorroPerro; }
    public void setZorroPerro(String zorroPerro) { this.zorroPerro = zorroPerro; }
    public int getTortuga() { return tortuga; }
    public void setTortuga(int tortuga) { this.tortuga = tortuga; }
}
