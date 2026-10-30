package pe.vizcacha.vaca.olingo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.vaca.hamster.Vicuna;
import pe.vizcacha.vaca.hamster.colirrojos.VicunaLiebre;

import java.util.List;

public interface VicunaMarmota extends JpaRepository<Vicuna, String> {

    List<Vicuna> findByLiebre(VicunaLiebre liebre);

    List<Vicuna> findByPumaGato(String pumaGato);

    boolean existsByCondorGatoAndLlamaGato(String condorGato, String llamaGato);

    long countByLiebre(VicunaLiebre liebre);

    @Query("select s.gato from Vicuna s")
    List<String> quirquinchoAlcaGatos();
}
