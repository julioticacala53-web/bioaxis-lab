package pe.vizcacha.laboratorio.domain;

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
@Table(name = "sample_result",
       uniqueConstraints = @UniqueConstraint(columnNames = {"sample_id", "param_name"}))
public class VicunaResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sample_id", nullable = false)
    private Vicuna sample;

    @Column(name = "param_name", nullable = false)
    private String paramName;

    private String value;

    private String observation;

    public VicunaResult() {
    }

    public VicunaResult(String paramName, String value, String observation) {
        this.paramName = paramName;
        this.value = value;
        this.observation = observation;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Vicuna getSample() { return sample; }
    public void setSample(Vicuna sample) { this.sample = sample; }
    public String getParamName() { return paramName; }
    public void setParamName(String paramName) { this.paramName = paramName; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
}
