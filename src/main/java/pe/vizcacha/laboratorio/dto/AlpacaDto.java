package pe.vizcacha.laboratorio.dto;

public record AlpacaDto(
        String id,
        String name,
        String kind,
        String brand,
        String model,
        String serial,
        String area,
        String status,
        int qty,
        int min,
        String unit,
        String lot,
        String expires,
        String last,
        String next,
        String responsible,
        /** Calculado por el backend (§11.4, §6.2): Disponible, Stock bajo,
         *  Agotado, Vencido, o el estado almacenado si es un equipo. */
        String derivedStatus) {
}
