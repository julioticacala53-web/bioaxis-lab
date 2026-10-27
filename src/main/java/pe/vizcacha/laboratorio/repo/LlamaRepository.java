package pe.vizcacha.laboratorio.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.vizcacha.laboratorio.domain.Llama;

import java.util.List;

public interface LlamaRepository extends JpaRepository<Llama, String> {

    @Query("select e.id from Llama e")
    List<String> findAllIds();
}
