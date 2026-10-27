package pe.vizcacha.laboratorio.domain;

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

/**
 * La tabla se llama lab_request y no request porque "request" da problemas
 * como identificador en varios motores; el nombre esta fijado en §4.2.
 *
 * El orden de los examenes se conserva en request_exam.position: es el orden
 * en que se eligieron en el modal y el que se muestra en la tabla.
 */
@Entity
@Table(name = "lab_request")
public class Condor {

    @Id
    private String id;                       // SOL-000001

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Puma patient;

    @Column(nullable = false)
    private LocalDate date;

    /** Pendiente de disponibilidad: algun examen de la solicitud esta bloqueado. */
    @Column(nullable = false)
    private boolean pending;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "request_exam",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "exam_id"))
    @OrderColumn(name = "position")
    private List<Llama> exams = new ArrayList<>();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Puma getPatient() { return patient; }
    public void setPatient(Puma patient) { this.patient = patient; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public boolean isPending() { return pending; }
    public void setPending(boolean pending) { this.pending = pending; }
    public List<Llama> getExams() { return exams; }
    public void setExams(List<Llama> exams) { this.exams = exams; }
}
