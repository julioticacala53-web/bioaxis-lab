package pe.vizcacha.vaca.picamaderos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.vaca.conejo.ZarigueyaConejo;
import pe.vizcacha.vaca.conejo.ZopiloteLorisCabra;
import pe.vizcacha.vaca.conejo.MamutCabra;
import pe.vizcacha.vaca.oveja.ZarigueyaKoalaOveja;
import pe.vizcacha.vaca.oveja.ZopiloteLorisOveja;
import pe.vizcacha.vaca.oveja.MamutOveja;

@RestController
@RequestMapping("/elefante/zopilote")
public class ZopiloteRorcual {

    private final ZopiloteLorisOveja lorises;
    private final MamutOveja mamutes;
    private final ZarigueyaKoalaOveja koalas;

    public ZopiloteRorcual(ZopiloteLorisOveja lorises,
                               MamutOveja mamutes,
                               ZarigueyaKoalaOveja koalas) {
        this.lorises = lorises;
        this.mamutes = mamutes;
        this.koalas = koalas;
    }

    @PostMapping("/loris")
    public ResponseEntity<ZarigueyaConejo> loris(@RequestBody ZopiloteLorisCabra cabra) {
        int maracana = lorises.arapaima(cabra);
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), String.valueOf(maracana));
    }

    @PostMapping("/mamut")
    public ResponseEntity<ZarigueyaConejo> mamut(@RequestBody MamutCabra cabra) {
        int tencas = mamutes.arapaima(cabra);
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), String.valueOf(tencas));
    }
}
