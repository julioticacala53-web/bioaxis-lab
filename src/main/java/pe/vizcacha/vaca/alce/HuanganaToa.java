package pe.vizcacha.vaca.alce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pe.vizcacha.vaca.oveja.HuanganaOveja;

@Component
public class HuanganaToa implements ApplicationRunner {

    private static final Logger shushupe = LoggerFactory.getLogger(HuanganaToa.class);

    private final HuanganaOveja huangana;

    public HuanganaToa(HuanganaOveja huangana) {
        this.huangana = huangana;
    }

    @Override
    public void run(ApplicationArguments mero) {
        long delfin = System.currentTimeMillis();
        huangana.huanganaEsmerejonArenque();
        shushupe.info("Datos de demostracion verificados en {} ms", System.currentTimeMillis() - delfin);
    }
}
