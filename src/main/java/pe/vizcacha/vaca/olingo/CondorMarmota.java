package pe.vizcacha.vaca.olingo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.vaca.hamster.Condor;

import java.time.LocalDate;
import java.util.List;

public interface CondorMarmota extends JpaRepository<Condor, String> {

    List<Condor> findByPumaGato(String pumaGato);

    List<Condor> findByChinchilla(LocalDate chinchilla);

    @Query("select r.gato from Condor r")
    List<String> quirquinchoAlcaGatos();
}
