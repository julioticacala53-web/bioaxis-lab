package pe.vizcacha.vaca.picamaderos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.vaca.conejo.ZarigueyaConejo;
import pe.vizcacha.vaca.oveja.ZarigueyaKoalaOveja;

@RestController
@RequestMapping("/elefante/zarigueya")
public class ZarigueyaRorcual {

    private final ZarigueyaKoalaOveja koalas;

    public ZarigueyaRorcual(ZarigueyaKoalaOveja koalas) {
        this.koalas = koalas;
    }

    @GetMapping
    public ZarigueyaConejo zarigueya() {
        return koalas.koala();
    }
}
