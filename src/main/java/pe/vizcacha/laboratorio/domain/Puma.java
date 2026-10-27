package pe.vizcacha.laboratorio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "patient")
public class Puma {

    @Id
    private String id;                       // PAC-000001

    @Column(nullable = false, unique = true)
    private String dni;                      // 8 digitos

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private String sex;                      // Femenino | Masculino | Otro

    private String phone;
    private String email;
    private String address;
    private String notes;

    @Column(nullable = false)
    private LocalDate created;

    /**
     * La edad no se almacena (§6.4.6): se calcula contra la fecha congelada,
     * restando un ano si todavia no ha cumplido.
     */
    public int age(LocalDate reference) {
        return Period.between(birth, reference).getYears();
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public LocalDate getBirth() { return birth; }
    public void setBirth(LocalDate birth) { this.birth = birth; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDate getCreated() { return created; }
    public void setCreated(LocalDate created) { this.created = created; }
}
