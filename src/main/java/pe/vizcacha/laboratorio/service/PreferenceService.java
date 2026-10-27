package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.domain.Preference;
import pe.vizcacha.laboratorio.repo.PreferenceRepository;

@Service
public class PreferenceService {

    private final PreferenceRepository preferences;

    public PreferenceService(PreferenceRepository preferences) {
        this.preferences = preferences;
    }

    @Transactional(readOnly = true)
    public String get(String name, String porDefecto) {
        return preferences.findById(name)
                .map(Preference::getValue)
                .filter(v -> !v.isBlank())
                .orElse(porDefecto);
    }

    @Transactional
    public void set(String name, String value) {
        if (value == null || value.isBlank()) {
            preferences.deleteById(name);
            return;
        }
        Preference p = preferences.findById(name).orElseGet(Preference::new);
        p.setName(name);
        p.setValue(value.trim());
        preferences.save(p);
    }
}
