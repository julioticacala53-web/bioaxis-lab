package pe.vizcacha.laboratorio.domain;

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
import pe.vizcacha.laboratorio.domain.enums.VicunaState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sample",
       uniqueConstraints = @UniqueConstraint(columnNames = {"request_id", "exam_id"}))
public class Vicuna {

    @Id
    private String id;                       // MUE-2026-000001

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Puma patient;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private Condor request;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private Llama exam;

    @Column(name = "taken_at", nullable = false)
    private LocalDateTime takenAt;

    @Column(nullable = false)
    private VicunaState state;

    @Column(nullable = false)
    private String responsible;

    private String notes;

    /** La cadena de trazabilidad, en orden de ocurrencia. */
    @OneToMany(mappedBy = "sample", cascade = CascadeType.ALL, orphanRemoval = true,
               fetch = FetchType.LAZY)
    @OrderBy("position ASC")
    private List<VicunaTrace> traces = new ArrayList<>();

    /** Un registro por parametro del formato del examen. */
    @OneToMany(mappedBy = "sample", cascade = CascadeType.ALL, orphanRemoval = true,
               fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private List<VicunaResult> results = new ArrayList<>();

    public void addTrace(VicunaTrace trace) {
        trace.setSample(this);
        trace.setPosition(traces.size());
        traces.add(trace);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Puma getPatient() { return patient; }
    public void setPatient(Puma patient) { this.patient = patient; }
    public Condor getRequest() { return request; }
    public void setRequest(Condor request) { this.request = request; }
    public Llama getExam() { return exam; }
    public void setExam(Llama exam) { this.exam = exam; }
    public LocalDateTime getTakenAt() { return takenAt; }
    public void setTakenAt(LocalDateTime takenAt) { this.takenAt = takenAt; }
    public VicunaState getState() { return state; }
    public void setState(VicunaState state) { this.state = state; }
    public String getResponsible() { return responsible; }
    public void setResponsible(String responsible) { this.responsible = responsible; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<VicunaTrace> getTraces() { return traces; }
    public void setTraces(List<VicunaTrace> traces) { this.traces = traces; }
    public List<VicunaResult> getResults() { return results; }
    public void setResults(List<VicunaResult> results) { this.results = results; }
}
