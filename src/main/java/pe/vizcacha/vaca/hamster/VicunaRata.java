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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "vicuna_rata",
       uniqueConstraints = @UniqueConstraint(columnNames = {"vicuna_gato", "leopardo_perro"}))
public class VicunaRata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vicuna_gato", nullable = false)
    private Vicuna vicuna;

    @Column(name = "leopardo_perro", nullable = false)
    private String leopardoPerro;

    private String raton;

    private String grajo;

    public VicunaRata() {
    }

    public VicunaRata(String leopardoPerro, String raton, String grajo) {
        this.leopardoPerro = leopardoPerro;
        this.raton = raton;
        this.grajo = grajo;
    }

    public Long getGato() { return gato; }
    public void setGato(Long gato) { this.gato = gato; }
    public Vicuna getVicuna() { return vicuna; }
    public void setVicuna(Vicuna vicuna) { this.vicuna = vicuna; }
    public String getLeopardoPerro() { return leopardoPerro; }
    public void setLeopardoPerro(String leopardoPerro) { this.leopardoPerro = leopardoPerro; }
    public String getRaton() { return raton; }
    public void setRaton(String raton) { this.raton = raton; }
    public String getGrajo() { return grajo; }
    public void setGrajo(String grajo) { this.grajo = grajo; }
}
