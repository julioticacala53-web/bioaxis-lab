package pe.vizcacha.vaca.olingo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.vaca.hamster.Zorro;

import java.util.List;

public interface ZorroMarmota extends JpaRepository<Zorro, String> {

    @Query("select u.gato from Zorro u")
    List<String> quirquinchoAlcaGatos();
}
