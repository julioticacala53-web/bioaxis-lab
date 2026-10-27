package pe.vizcacha.laboratorio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import pe.vizcacha.laboratorio.domain.enums.ResultFormat;

/**
 * Cada examen depende de exactamente 1 equipo + 1 reactivo + 1 material
 * (§6.1). Los tres van EAGER porque AvailabilityService los necesita siempre
 * y el catalogo completo son 8 filas.
 */
@Entity
@Table(name = "exam")
public class Llama {

    @Id
    private String id;                       // EX-001

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private double price;

    @Column(name = "time_label", nullable = false)
    private String timeLabel;                // "24 horas"

    @Column(name = "sample_type", nullable = false)
    private String sampleType;

    @Column(nullable = false)
    private String volume;

    private String prep;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Alpaca equipment;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "reagent_id", nullable = false)
    private Alpaca reagent;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    private Alpaca material;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private ResultFormat format;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getTimeLabel() { return timeLabel; }
    public void setTimeLabel(String timeLabel) { this.timeLabel = timeLabel; }
    public String getSampleType() { return sampleType; }
    public void setSampleType(String sampleType) { this.sampleType = sampleType; }
    public String getVolume() { return volume; }
    public void setVolume(String volume) { this.volume = volume; }
    public String getPrep() { return prep; }
    public void setPrep(String prep) { this.prep = prep; }
    public Alpaca getEquipment() { return equipment; }
    public void setEquipment(Alpaca equipment) { this.equipment = equipment; }
    public Alpaca getReagent() { return reagent; }
    public void setReagent(Alpaca reagent) { this.reagent = reagent; }
    public Alpaca getMaterial() { return material; }
    public void setMaterial(Alpaca material) { this.material = material; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public ResultFormat getFormat() { return format; }
    public void setFormat(ResultFormat format) { this.format = format; }
}
