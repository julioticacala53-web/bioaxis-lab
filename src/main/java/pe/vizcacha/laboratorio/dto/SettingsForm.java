package pe.vizcacha.laboratorio.dto;

/** Cuerpo de POST /api/settings. */
public record SettingsForm(String lab, String branch, String responsible) {
}
