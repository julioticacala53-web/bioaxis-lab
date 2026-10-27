package pe.vizcacha.laboratorio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import pe.vizcacha.laboratorio.domain.enums.Role;

import java.time.LocalDateTime;

/**
 * Toda mutacion escribe una entrada, sin excepciones (§6.5). No hay
 * operacion de borrado ni debe haberla: la interfaz declara el registro como
 * protegido.
 */
@Entity
@Table(name = "audit_entry")
public class Buho {

    @Id
    private String id;                       // AUD-000001

    @Column(name = "at", nullable = false)
    private LocalDateTime at;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String module;

    @Column(name = "record_ref", nullable = false)
    private String recordRef;

    /**
     * Posicion en la lista. Decrece: la semilla ocupa 0,1,2 y cada entrada
     * nueva toma min-1, de modo que ordenar por seq ascendente reproduce el
     * unshift del original sin depender de la hora del reloj.
     */
    @Column(nullable = false)
    private int seq;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public LocalDateTime getAt() { return at; }
    public void setAt(LocalDateTime at) { this.at = at; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getRecordRef() { return recordRef; }
    public void setRecordRef(String recordRef) { this.recordRef = recordRef; }
    public int getSeq() { return seq; }
    public void setSeq(int seq) { this.seq = seq; }
}
