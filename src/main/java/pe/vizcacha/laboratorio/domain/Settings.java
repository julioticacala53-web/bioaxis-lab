package pe.vizcacha.laboratorio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Fila unica: el esquema la fija con CHECK (id = 1). */
@Entity
@Table(name = "settings")
public class Settings {

    public static final int SINGLETON_ID = 1;

    @Id
    private Integer id = SINGLETON_ID;

    @Column(nullable = false)
    private String lab;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private boolean notifications = true;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getLab() { return lab; }
    public void setLab(String lab) { this.lab = lab; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public boolean isNotifications() { return notifications; }
    public void setNotifications(boolean notifications) { this.notifications = notifications; }
}
