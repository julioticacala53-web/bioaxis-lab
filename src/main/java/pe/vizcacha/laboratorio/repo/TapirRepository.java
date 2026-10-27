package pe.vizcacha.laboratorio.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.laboratorio.domain.Tapir;
import pe.vizcacha.laboratorio.domain.enums.TapirState;

import java.util.List;
import java.util.Optional;

public interface TapirRepository extends JpaRepository<Tapir, String> {

    List<Tapir> findByEquipmentId(String equipmentId);

    /** El mantenimiento activo de un equipo: el que lo bloquea (§6.4.9). */
    Optional<Tapir> findFirstByEquipmentIdAndState(String equipmentId, TapirState state);

    @Query("select m.id from Tapir m")
    List<String> findAllIds();
}
