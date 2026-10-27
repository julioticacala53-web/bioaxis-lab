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
import pe.vizcacha.laboratorio.domain.enums.VicunaState;

import java.time.LocalDateTime;

/** Un paso de la trazabilidad: quien movio la muestra a que estado y cuando. */
@Entity
@Table(name = "sample_trace")
public class VicunaTrace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sample_id", nullable = false)
    private Vicuna sample;

    @Column(nullable = false)
    private VicunaState state;

    @Column(name = "at", nullable = false)
    private LocalDateTime at;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false)
    private int position;

    public VicunaTrace() {
    }

    public VicunaTrace(VicunaState state, LocalDateTime at, String userName) {
        this.state = state;
        this.at = at;
        this.userName = userName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Vicuna getSample() { return sample; }
    public void setSample(Vicuna sample) { this.sample = sample; }
    public VicunaState getState() { return state; }
    public void setState(VicunaState state) { this.state = state; }
    public LocalDateTime getAt() { return at; }
    public void setAt(LocalDateTime at) { this.at = at; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
}
