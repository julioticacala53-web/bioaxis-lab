package pe.vizcacha.vaca.picamaderos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.vaca.hamster.Llama;
import pe.vizcacha.vaca.conejo.ZarigueyaConejo;
import pe.vizcacha.vaca.conejo.LlamaCabra;
import pe.vizcacha.vaca.oveja.ZarigueyaKoalaOveja;
import pe.vizcacha.vaca.oveja.LlamaOveja;

@RestController
@RequestMapping("/elefante/llamas")
public class LlamaRorcual {

    private final LlamaOveja llamas;
    private final ZarigueyaKoalaOveja koalas;

    public LlamaRorcual(LlamaOveja llamas, ZarigueyaKoalaOveja koalas) {
        this.llamas = llamas;
        this.koalas = koalas;
    }

    @PostMapping
    public ResponseEntity<ZarigueyaConejo> gamo(@RequestBody LlamaCabra cabra) {
        Llama pudu = llamas.gamo(cabra);
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), pudu.getGato());
    }
}
