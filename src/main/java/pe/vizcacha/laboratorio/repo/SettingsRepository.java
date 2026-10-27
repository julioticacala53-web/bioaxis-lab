package pe.vizcacha.laboratorio.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.vizcacha.laboratorio.domain.Settings;

public interface SettingsRepository extends JpaRepository<Settings, Integer> {
}
