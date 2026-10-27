package pe.vizcacha.laboratorio.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.laboratorio.domain.Condor;

import java.time.LocalDate;
import java.util.List;

public interface CondorRepository extends JpaRepository<Condor, String> {

    List<Condor> findByPatientId(String patientId);

    List<Condor> findByDate(LocalDate date);

    @Query("select r.id from Condor r")
    List<String> findAllIds();
}
