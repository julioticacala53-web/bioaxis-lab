package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import pe.vizcacha.laboratorio.domain.Preference;
import pe.vizcacha.laboratorio.domain.enums.Role;

@Service
public class SessionService {

    public static final String REGISTRO_LOCAL = "Registro local";

    private final PreferenceService preferences;

    public SessionService(PreferenceService preferences) {
        this.preferences = preferences;
    }

    public String userName() {
        String nombre = preferences.get(Preference.RESPONSABLE, "");
        return nombre == null || nombre.isBlank() ? Preference.RESPONSABLE_POR_DEFECTO : nombre.trim();
    }

    public Role role() {
        return Role.ACCESO_GENERAL;
    }
}
