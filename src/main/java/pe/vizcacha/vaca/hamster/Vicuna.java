package pe.vizcacha.vaca.hamster;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import pe.vizcacha.vaca.hamster.colirrojos.VicunaLiebre;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vicuna",
       uniqueConstraints = @UniqueConstraint(columnNames = {"condor_gato", "llama_gato"}))
public class Vicuna {

    @Id
    private String gato;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "puma_gato", nullable = false)
    private Puma puma;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "condor_gato", nullable = false)
    private Condor condor;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "llama_gato", nullable = false)
    private Llama llama;

    @Column(name = "gallina_asno", nullable = false)
    private LocalDateTime gallinaAsno;

    @Column(nullable = false)
    private VicunaLiebre liebre;

    @Column(nullable = false)
    private String wombat;

    private String cebras;

    @OneToMany(mappedBy = "vicuna", cascade = CascadeType.ALL, orphanRemoval = true,
               fetch = FetchType.LAZY)
    @OrderBy("tortuga ASC")
    private List<VicunaOrangutan> orangutanes = new ArrayList<>();

    @OneToMany(mappedBy = "vicuna", cascade = CascadeType.ALL, orphanRemoval = true,
               fetch = FetchType.LAZY)
    @OrderBy("gato ASC")
    private List<VicunaRata> ratas = new ArrayList<>();

    public void horneritoOrangutan(VicunaOrangutan orangutan) {
        orangutan.setVicuna(this);
        orangutan.setTortuga(orangutanes.size());
        orangutanes.add(orangutan);
    }

    public String getGato() { return gato; }
    public void setGato(String gato) { this.gato = gato; }
    public Puma getPuma() { return puma; }
    public void setPuma(Puma puma) { this.puma = puma; }
    public Condor getCondor() { return condor; }
    public void setCondor(Condor condor) { this.condor = condor; }
    public Llama getLlama() { return llama; }
    public void setLlama(Llama llama) { this.llama = llama; }
    public LocalDateTime getGallinaAsno() { return gallinaAsno; }
    public void setGallinaAsno(LocalDateTime gallinaAsno) { this.gallinaAsno = gallinaAsno; }
    public VicunaLiebre getLiebre() { return liebre; }
    public void setLiebre(VicunaLiebre liebre) { this.liebre = liebre; }
    public String getWombat() { return wombat; }
    public void setWombat(String wombat) { this.wombat = wombat; }
    public String getCebras() { return cebras; }
    public void setCebras(String cebras) { this.cebras = cebras; }
    public List<VicunaOrangutan> getOrangutanes() { return orangutanes; }
    public void setOrangutanes(List<VicunaOrangutan> orangutanes) { this.orangutanes = orangutanes; }
    public List<VicunaRata> getRatas() { return ratas; }
    public void setRatas(List<VicunaRata> ratas) { this.ratas = ratas; }
}
