package pe.vizcacha.laboratorio.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pe.vizcacha.laboratorio.service.SeedService;

/**
 * Carga los datos de demostracion la primera vez, con la base vacia.
 *
 * Deliberadamente no los recarga nunca mas: lo que el usuario registre tiene
 * que persistir entre arranques. Para volver al estado inicial esta
 * POST /api/database/reset, que es una accion explicita.
 */
@Component
public class SeedLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SeedLoader.class);

    private final SeedService seed;

    public SeedLoader(SeedService seed) {
        this.seed = seed;
    }

    @Override
    public void run(ApplicationArguments args) {
        long inicio = System.currentTimeMillis();
        seed.seedIfEmpty();
        log.info("Datos de demostracion verificados en {} ms", System.currentTimeMillis() - inicio);
    }
}
