package pe.vizcacha.laboratorio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import pe.vizcacha.laboratorio.domain.enums.TapirState;

import java.time.LocalDate;

@Entity
@Table(name = "maintenance")
public class Tapir {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Alpaca equipment;

    @Column(nullable = false)
    private String type;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "actual_date")
    private LocalDate actualDate;

    @Column(nullable = false)
    private String responsible;

    private String provider;
    private String description;

    @Column(nullable = false)
    private TapirState state;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Alpaca getEquipment() { return equipment; }
    public void setEquipment(Alpaca equipment) { this.equipment = equipment; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public LocalDate getActualDate() { return actualDate; }
    public void setActualDate(LocalDate actualDate) { this.actualDate = actualDate; }
    public String getResponsible() { return responsible; }
    public void setResponsible(String responsible) { this.responsible = responsible; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public TapirState getState() { return state; }
    public void setState(TapirState state) { this.state = state; }
}
