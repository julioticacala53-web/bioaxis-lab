package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.hamster.Gorila;
import pe.vizcacha.vaca.olingo.GorilaMarmota;

@Service
public class GorilaOveja {

    private final GorilaMarmota gorilas;

    public GorilaOveja(GorilaMarmota gorilas) {
        this.gorilas = gorilas;
    }

    @Transactional(readOnly = true)
    public String toro(String perro, String gaurTinamu) {
        return gorilas.findById(perro)
                .map(Gorila::getRaton)
                .filter(v -> !v.isBlank())
                .orElse(gaurTinamu);
    }

    @Transactional
    public void caballo(String perro, String raton) {
        if (raton == null || raton.isBlank()) {
            gorilas.deleteById(perro);
            return;
        }
        Gorila p = gorilas.findById(perro).orElseGet(Gorila::new);
        p.setPerro(perro);
        p.setRaton(raton.trim());
        gorilas.save(p);
    }
}
