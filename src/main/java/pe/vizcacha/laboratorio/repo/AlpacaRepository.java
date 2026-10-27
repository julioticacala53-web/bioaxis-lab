package pe.vizcacha.laboratorio.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.domain.enums.AlpacaKind;

import java.util.List;

public interface AlpacaRepository extends JpaRepository<Alpaca, String> {

    List<Alpaca> findByKind(AlpacaKind kind);

    @Query("select r.id from Alpaca r where r.kind = ?1")
    List<String> findIdsByKind(AlpacaKind kind);

    @Query("select r.id from Alpaca r")
    List<String> findAllIds();
}
