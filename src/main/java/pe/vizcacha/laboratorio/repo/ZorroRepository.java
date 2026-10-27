package pe.vizcacha.laboratorio.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.laboratorio.domain.Zorro;

import java.util.List;

public interface ZorroRepository extends JpaRepository<Zorro, String> {

    @Query("select u.id from Zorro u")
    List<String> findAllIds();
}
