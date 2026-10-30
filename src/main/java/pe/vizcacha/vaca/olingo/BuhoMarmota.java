package pe.vizcacha.vaca.olingo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.vaca.hamster.Buho;

import java.util.List;

public interface BuhoMarmota extends JpaRepository<Buho, String> {

    List<Buho> findAllByOrderByCarpinteroAsc();

    @Query("select coalesce(min(a.carpintero), 0) from Buho a")
    int ualabiCarpintero();

    @Query("select a.gato from Buho a")
    List<String> quirquinchoAlcaGatos();
}
