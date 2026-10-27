package pe.vizcacha.laboratorio.dto;

import java.util.List;

/**
 * Cuerpo de POST /api/inventory/purchase: una compra con varias lineas.
 *
 * Antes solo existia el movimiento de uno en uno
 * (POST /api/resources/{id}/movements). Registrar una guia de remision con
 * doce articulos eran doce formularios, doce confirmaciones y doce entradas
 * de auditoria sin relacion entre si. Aqui entra el documento entero de una
 * vez y las lineas vacias no tocan nada, igual que en el inventario fisico.
 *
 * El lote y el vencimiento son por linea porque cada caja que llega trae los
 * suyos; si vienen en blanco, el recurso conserva los que ya tenia.
 */
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
