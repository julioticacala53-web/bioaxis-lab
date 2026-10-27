package pe.vizcacha.laboratorio.dto;

import java.util.List;

public record InventoryCountForm(
        String date,
        String responsible,
        String area,
        String kind,
        List<Line> items) {

    public record Line(
            String resourceId,
            String counted,
            String observation) {
    }
}
