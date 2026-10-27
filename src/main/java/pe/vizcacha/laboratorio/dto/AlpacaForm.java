package pe.vizcacha.laboratorio.dto;

/**
 * Cuerpo de POST /api/resources. id vacio significa alta nueva, y el prefijo
 * del identificador se decide por kind: EQ-, REA- o MAT-.
 */
public record AlpacaForm(
        String id,
        String kind,
        String name,
        String brand,
        String model,
        String serial,
        String area,
        String status,
        Integer qty,
        Integer min,
        String unit,
        String lot,
        String expires,
        String last,
        String next,
        String responsible) {
}
