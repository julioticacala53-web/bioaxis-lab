package pe.vizcacha.laboratorio.dto;

import java.util.List;

public record PurchaseForm(
        String date,
        String supplier,
        String document,
        String responsible,
        String kind,
        List<Line> items) {

    public record Line(
            String resourceId,
            String qty,
            String lot,
            String expires) {
    }
}
