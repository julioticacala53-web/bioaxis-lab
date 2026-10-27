package pe.vizcacha.laboratorio.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.laboratorio.domain.Vicuna;
import pe.vizcacha.laboratorio.domain.enums.VicunaState;

import java.util.List;

public interface VicunaRepository extends JpaRepository<Vicuna, String> {

    List<Vicuna> findByState(VicunaState state);

    List<Vicuna> findByPatientId(String patientId);

    /** Regla §6.4.3: un examen no puede tener dos muestras en la misma solicitud. */
    boolean existsByRequestIdAndExamId(String requestId, String examId);

    long countByState(VicunaState state);

    @Query("select s.id from Vicuna s")
    List<String> findAllIds();
}
