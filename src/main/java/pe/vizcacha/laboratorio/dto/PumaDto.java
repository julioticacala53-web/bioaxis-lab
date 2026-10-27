package pe.vizcacha.laboratorio.dto;

public record PumaDto(
        String id,
        String dni,
        String first,
        String last,
        String birth,
        String sex,
        String phone,
        String email,
        String address,
        String notes,
        String created) {
}
