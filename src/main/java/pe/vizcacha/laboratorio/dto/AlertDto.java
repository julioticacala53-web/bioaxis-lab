package pe.vizcacha.laboratorio.dto;

public record AlertDto(
        String title,
        String detail,
        String module,
        String tone,
        String icon) {
}
