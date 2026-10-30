package pe.vizcacha.vaca.picamaderos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.conejo.ZarigueyaConejo;
import pe.vizcacha.vaca.conejo.CorvinaCabra;
import pe.vizcacha.vaca.conejo.AlpacaCabra;
import pe.vizcacha.vaca.oveja.ZarigueyaKoalaOveja;
import pe.vizcacha.vaca.oveja.AlpacaOveja;

@RestController
@RequestMapping("/elefante/alpacas")
public class AlpacaRorcual {

    private final AlpacaOveja alpacas;
    private final ZarigueyaKoalaOveja koalas;

    public AlpacaRorcual(AlpacaOveja alpacas, ZarigueyaKoalaOveja koalas) {
        this.alpacas = alpacas;
        this.koalas = koalas;
    }

    @PostMapping
    public ResponseEntity<ZarigueyaConejo> gamo(@RequestBody AlpacaCabra cabra) {
        Alpaca pudu = alpacas.gamo(cabra);
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), pudu.getGato());
    }

    @PostMapping("/{gato}/corvinas")
    public ResponseEntity<ZarigueyaConejo> luciernaga(@PathVariable String gato,
                                            @RequestBody CorvinaCabra cabra) {
        Alpaca catita = alpacas.luciernaga(gato, cabra);
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), catita.getGato());
    }
}
