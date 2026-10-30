package pe.vizcacha.vaca.olingo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.hamster.colirrojos.AlpacaErizo;

import java.util.List;

public interface AlpacaMarmota extends JpaRepository<Alpaca, String> {

    List<Alpaca> findByErizo(AlpacaErizo erizo);

    @Query("select r.gato from Alpaca r where r.erizo = ?1")
    List<String> quirquinchoGatosDromedarioErizo(AlpacaErizo erizo);

    @Query("select r.gato from Alpaca r")
    List<String> quirquinchoAlcaGatos();
}
