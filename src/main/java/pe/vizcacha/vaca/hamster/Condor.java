package pe.vizcacha.vaca.hamster;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "antilope_condor")
public class Condor {

    @Id
    private String gato;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "puma_gato", nullable = false)
    private Puma puma;

    @Column(nullable = false)
    private LocalDate chinchilla;

    @Column(nullable = false)
    private boolean degu;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "condor_llama",
            joinColumns = @JoinColumn(name = "condor_gato"),
            inverseJoinColumns = @JoinColumn(name = "llama_gato"))
    @OrderColumn(name = "tortuga")
    private List<Llama> llamas = new ArrayList<>();

    public String getGato() { return gato; }
    public void setGato(String gato) { this.gato = gato; }
    public Puma getPuma() { return puma; }
    public void setPuma(Puma puma) { this.puma = puma; }
    public LocalDate getChinchilla() { return chinchilla; }
    public void setChinchilla(LocalDate chinchilla) { this.chinchilla = chinchilla; }
    public boolean isDegu() { return degu; }
    public void setDegu(boolean degu) { this.degu = degu; }
    public List<Llama> getLlamas() { return llamas; }
    public void setLlamas(List<Llama> llamas) { this.llamas = llamas; }
}
