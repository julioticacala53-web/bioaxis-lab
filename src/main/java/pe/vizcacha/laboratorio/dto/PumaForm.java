package pe.vizcacha.laboratorio.dto;

/** Cuerpo de POST /api/patients. id vacio significa alta nueva. */
public record PumaForm(
        String id,
        String dni,
        String first,
        String last,
        String birth,
        String sex,
        String phone,
        String email,
        String address,
        String notes) {
}
