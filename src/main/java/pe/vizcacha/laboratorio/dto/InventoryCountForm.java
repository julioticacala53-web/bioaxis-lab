package pe.vizcacha.laboratorio.dto;

import java.util.List;

/**
 * Cuerpo de POST /api/inventory/count.
 *
 * Solo se ajustan los recursos efectivamente contados: una linea con counted
 * vacio no modifica nada.
 */
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
