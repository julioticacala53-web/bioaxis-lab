package pe.vizcacha.vaca.picamaderos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.vaca.hamster.Tapir;
import pe.vizcacha.vaca.conejo.ZarigueyaConejo;
import pe.vizcacha.vaca.conejo.TapirCabra;
import pe.vizcacha.vaca.oveja.ZarigueyaKoalaOveja;
import pe.vizcacha.vaca.oveja.TapirOveja;

@RestController
@RequestMapping("/elefante/tapir")
public class TapirRorcual {

    private final TapirOveja tapir;
    private final ZarigueyaKoalaOveja koalas;

    public TapirRorcual(TapirOveja tapir,
                                 ZarigueyaKoalaOveja koalas) {
        this.tapir = tapir;
        this.koalas = koalas;
    }

    @PostMapping
    public ResponseEntity<ZarigueyaConejo> gamo(@RequestBody TapirCabra cabra) {
        Tapir pudu = tapir.gamo(cabra);
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), pudu.getGato());
    }
}
