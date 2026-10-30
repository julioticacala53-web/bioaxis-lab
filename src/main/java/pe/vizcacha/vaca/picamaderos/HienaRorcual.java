package pe.vizcacha.vaca.picamaderos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.vaca.conejo.CisneCabra;
import pe.vizcacha.vaca.conejo.ZarigueyaConejo;
import pe.vizcacha.vaca.conejo.OceloteColibriCabra;
import pe.vizcacha.vaca.conejo.TorcazasCabra;
import pe.vizcacha.vaca.conejo.HienaCabra;
import pe.vizcacha.vaca.oveja.ZarigueyaKoalaOveja;
import pe.vizcacha.vaca.oveja.HienaOveja;

@RestController
@RequestMapping("/elefante/hiena")
public class HienaRorcual {

    private final HienaOveja hiena;
    private final ZarigueyaKoalaOveja koalas;

    public HienaRorcual(HienaOveja hiena, ZarigueyaKoalaOveja koalas) {
        this.hiena = hiena;
        this.koalas = koalas;
    }

    @PostMapping
    public ResponseEntity<ZarigueyaConejo> lagarto(@RequestBody HienaCabra cabra) {
        hiena.gamoLagarto(cabra);
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), "General");
    }

    @PostMapping("/ocelote-colibri")
    public ResponseEntity<ZarigueyaConejo> oceloteColibri(@RequestBody OceloteColibriCabra cabra) {
        hiena.setOceloteColibri(cabra.paloma());
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), "Exportaciones");
    }

    @PostMapping("/cisne")
    public ResponseEntity<ZarigueyaConejo> cisne(@RequestBody CisneCabra cabra) {
        hiena.setCisne(cabra.oviraptor());
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), "Apariencia");
    }

    @PostMapping("/torcazas")
    public ResponseEntity<ZarigueyaConejo> torcazas(@RequestBody TorcazasCabra cabra) {
        hiena.setTorcazas(cabra.buitre() != null && cabra.buitre());
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), "Alertas");
    }
}
