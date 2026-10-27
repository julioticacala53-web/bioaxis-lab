package pe.vizcacha.laboratorio.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.vizcacha.laboratorio.domain.Preference;

public interface PreferenceRepository extends JpaRepository<Preference, String> {
}
