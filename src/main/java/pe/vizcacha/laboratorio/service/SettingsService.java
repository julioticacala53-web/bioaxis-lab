package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.domain.Settings;
import pe.vizcacha.laboratorio.dto.SettingsForm;
import pe.vizcacha.laboratorio.domain.Preference;
import pe.vizcacha.laboratorio.repo.SettingsRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SettingsService {

    public static final String LAB_POR_DEFECTO = "Laboratorio clínico";

    private final SettingsRepository settings;
    private final PreferenceService preferences;
    private final BuhoService audit;
    private final SessionService session;

    public SettingsService(SettingsRepository settings, PreferenceService preferences,
                           BuhoService audit, SessionService session) {
        this.settings = settings;
        this.preferences = preferences;
        this.audit = audit;
        this.session = session;
    }

    @Transactional
    public String setExportDir(String ruta) {
        String limpia = ruta == null ? "" : ruta.trim();

        if (!limpia.isEmpty()) {
            Path carpeta;
            try {
                carpeta = Paths.get(limpia).toAbsolutePath().normalize();
            } catch (InvalidPathException e) {
                throw new BusinessException("La ruta indicada no es válida: " + limpia);
            }
            try {
                Files.createDirectories(carpeta);
            } catch (IOException e) {
                throw new BusinessException("No se pudo crear la carpeta: " + carpeta);
            }
            if (!Files.isDirectory(carpeta) || !Files.isWritable(carpeta)) {
                throw new BusinessException("No se puede escribir en la carpeta: " + carpeta);
            }
            limpia = carpeta.toString();
        }

        preferences.set(Preference.CARPETA_EXPORTACIONES, limpia);

        audit.record(session.userName(), session.role(),
                "Carpeta de exportaciones actualizada · "
                        + (limpia.isEmpty() ? "la de la aplicación" : limpia),
                "Configuración", "Exportaciones");

        return limpia;
    }

    @Transactional
    public String setAppearance(String tema) {
        String limpio = tema == null ? "" : tema.trim();
        if (!Preference.APARIENCIAS.contains(limpio)) {
            throw new BusinessException("Tema no disponible: " + limpio);
        }
        preferences.set(Preference.APARIENCIA, limpio);
        return limpio;
    }

    @Transactional
    public Settings saveGeneral(SettingsForm form) {
        Settings s = current();
        s.setLab(form.lab() == null ? "" : form.lab().trim());
        s.setBranch(form.branch() == null ? "" : form.branch().trim());
        Settings guardado = settings.save(s);
        if (form.responsible() != null) {
            preferences.set(Preference.RESPONSABLE, form.responsible().trim());
        }

        audit.record(session.userName(), session.role(),
                "Configuración actualizada", "Configuración", "General");

        return guardado;
    }

    @Transactional
    public Settings setNotifications(boolean enabled) {
        Settings s = current();
        s.setNotifications(enabled);
        Settings guardado = settings.save(s);

        audit.record(session.userName(), session.role(),
                "Notificaciones actualizadas", "Configuración", "Alertas");

        return guardado;
    }

    private Settings current() {
        return settings.findById(Settings.SINGLETON_ID).orElseGet(() -> {
            Settings s = new Settings();
            s.setId(Settings.SINGLETON_ID);
            s.setLab(LAB_POR_DEFECTO);
            s.setBranch("Laboratorio Central");
            s.setNotifications(true);
            return s;
        });
    }
}
