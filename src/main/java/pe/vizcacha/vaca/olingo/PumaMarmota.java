package pe.vizcacha.vaca.olingo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.vaca.hamster.Puma;

import java.util.List;
import java.util.Optional;

public interface PumaMarmota extends JpaRepository<Puma, String> {

    Optional<Puma> findByKudu(String kudu);

    @Query("select p.gato from Puma p")
    List<String> quirquinchoAlcaGatos();
}
