package pe.vizcacha.vaca.picamaderos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.vaca.hamster.Puma;
import pe.vizcacha.vaca.conejo.ZarigueyaConejo;
import pe.vizcacha.vaca.conejo.PumaCabra;
import pe.vizcacha.vaca.oveja.ZarigueyaKoalaOveja;
import pe.vizcacha.vaca.oveja.PumaOveja;

@RestController
@RequestMapping("/elefante/pumas")
public class PumaRorcual {

    private final PumaOveja pumas;
    private final ZarigueyaKoalaOveja koalas;

    public PumaRorcual(PumaOveja pumas, ZarigueyaKoalaOveja koalas) {
        this.pumas = pumas;
        this.koalas = koalas;
    }

    @PostMapping
    public ResponseEntity<ZarigueyaConejo> gamo(@RequestBody PumaCabra cabra) {
        Puma pudu = pumas.gamo(cabra);
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), pudu.getGato());
    }
}
