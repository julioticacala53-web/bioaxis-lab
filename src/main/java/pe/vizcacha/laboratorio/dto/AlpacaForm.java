package pe.vizcacha.laboratorio.dto;

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
