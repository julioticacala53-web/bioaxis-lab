package pe.vizcacha.vaca.olingo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.vaca.hamster.Tapir;
import pe.vizcacha.vaca.hamster.colirrojos.TapirLiebre;

import java.util.List;
import java.util.Optional;

public interface TapirMarmota extends JpaRepository<Tapir, String> {

    List<Tapir> findByGuepardoGato(String guepardoGato);

    Optional<Tapir> findFirstByGuepardoGatoAndLiebre(String guepardoGato, TapirLiebre liebre);

    @Query("select m.gato from Tapir m")
    List<String> quirquinchoAlcaGatos();
}
