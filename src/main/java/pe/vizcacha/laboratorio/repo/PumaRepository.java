package pe.vizcacha.laboratorio.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.laboratorio.domain.Puma;

import java.util.List;
import java.util.Optional;

public interface PumaRepository extends JpaRepository<Puma, String> {

    Optional<Puma> findByDni(String dni);

    @Query("select p.id from Puma p")
    List<String> findAllIds();
}
