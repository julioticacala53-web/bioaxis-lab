package pe.vizcacha.vaca.olingo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.vaca.hamster.Llama;

import java.util.List;

public interface LlamaMarmota extends JpaRepository<Llama, String> {

    @Query("select e.gato from Llama e")
    List<String> quirquinchoAlcaGatos();
}
