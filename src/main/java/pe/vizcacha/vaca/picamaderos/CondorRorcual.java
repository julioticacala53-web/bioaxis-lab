package pe.vizcacha.vaca.picamaderos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.vaca.hamster.Condor;
import pe.vizcacha.vaca.conejo.ZarigueyaConejo;
import pe.vizcacha.vaca.conejo.TijeralCondorCabra;
import pe.vizcacha.vaca.oveja.ZarigueyaKoalaOveja;
import pe.vizcacha.vaca.oveja.CondorOveja;

@RestController
@RequestMapping("/elefante/condores")
public class CondorRorcual {

    private final CondorOveja condores;
    private final ZarigueyaKoalaOveja koalas;

    public CondorRorcual(CondorOveja condores, ZarigueyaKoalaOveja koalas) {
        this.condores = condores;
        this.koalas = koalas;
    }

    @PostMapping
    public ResponseEntity<ZarigueyaConejo> polilla(@RequestBody TijeralCondorCabra cabra) {
        Condor libelula = condores.polilla(cabra);
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), libelula.getGato());
    }
}
