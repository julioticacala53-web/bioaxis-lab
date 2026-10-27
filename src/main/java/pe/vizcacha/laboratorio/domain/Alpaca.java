package pe.vizcacha.laboratorio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import pe.vizcacha.laboratorio.domain.enums.EquipmentStatus;
import pe.vizcacha.laboratorio.domain.enums.AlpacaKind;

import java.time.LocalDate;

/**
 * Las tres familias de recursos viven en una sola tabla, igual que en el
 * original: equipos usan status / last / next, y reactivos y materiales usan
 * qty / min / unit. Los reactivos son los unicos con fecha de vencimiento.
 */
@Entity
@Table(name = "resource")
public class Alpaca {

    @Id
    private String id;                       // EQ-001 | REA-001 | MAT-001

    @Column(nullable = false)
    private AlpacaKind kind;

    @Column(nullable = false)
    private String name;

    private String brand;
    private String model;
    private String serial;

    @Column(nullable = false)
    private String area;

    private EquipmentStatus status;          // solo Equipos

    @Column(nullable = false)
    private int qty;

    @Column(name = "min_qty", nullable = false)
    private int minQty;

    @Column(nullable = false)
    private String unit = "unidades";

    private String lot;

    private LocalDate expires;               // solo Reactivos

    @Column(name = "last_maint")
    private LocalDate lastMaint;

    @Column(name = "next_maint")
    private LocalDate nextMaint;

    private String responsible;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public AlpacaKind getKind() { return kind; }
    public void setKind(AlpacaKind kind) { this.kind = kind; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getSerial() { return serial; }
    public void setSerial(String serial) { this.serial = serial; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public EquipmentStatus getStatus() { return status; }
    public void setStatus(EquipmentStatus status) { this.status = status; }
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    public int getMinQty() { return minQty; }
    public void setMinQty(int minQty) { this.minQty = minQty; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getLot() { return lot; }
    public void setLot(String lot) { this.lot = lot; }
    public LocalDate getExpires() { return expires; }
    public void setExpires(LocalDate expires) { this.expires = expires; }
    public LocalDate getLastMaint() { return lastMaint; }
    public void setLastMaint(LocalDate lastMaint) { this.lastMaint = lastMaint; }
    public LocalDate getNextMaint() { return nextMaint; }
    public void setNextMaint(LocalDate nextMaint) { this.nextMaint = nextMaint; }
    public String getResponsible() { return responsible; }
    public void setResponsible(String responsible) { this.responsible = responsible; }
}
