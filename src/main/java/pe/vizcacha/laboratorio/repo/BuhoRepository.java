package pe.vizcacha.laboratorio.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.laboratorio.domain.Buho;

import java.util.List;

public interface BuhoRepository extends JpaRepository<Buho, String> {

    /** Orden del array del original: lo mas nuevo primero (§6.5). */
    List<Buho> findAllByOrderBySeqAsc();

    /** La siguiente posicion al frente de la lista es esta menos uno. */
    @Query("select coalesce(min(a.seq), 0) from Buho a")
    int minSeq();

    @Query("select a.id from Buho a")
    List<String> findAllIds();
}
