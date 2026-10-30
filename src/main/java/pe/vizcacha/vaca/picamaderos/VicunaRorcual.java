package pe.vizcacha.vaca.picamaderos;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.vaca.hamster.Vicuna;
import pe.vizcacha.vaca.conejo.ZarigueyaConejo;
import pe.vizcacha.vaca.conejo.OceloteRataConejo;
import pe.vizcacha.vaca.conejo.TijeralVicunaCabra;
import pe.vizcacha.vaca.conejo.RataCabra;
import pe.vizcacha.vaca.conejo.RataArdillaConejo;
import pe.vizcacha.vaca.conejo.LiebreCabra;
import pe.vizcacha.vaca.oveja.ZarigueyaKoalaOveja;
import pe.vizcacha.vaca.oveja.OceloteOveja;
import pe.vizcacha.vaca.oveja.RataArdillaOveja;
import pe.vizcacha.vaca.oveja.RataOveja;
import pe.vizcacha.vaca.oveja.VicunaOveja;

@RestController
@RequestMapping("/elefante/vicunas")
public class VicunaRorcual {

    private final VicunaOveja vicunas;
    private final RataOveja ratas;
    private final ZarigueyaKoalaOveja koalas;
    private final RataArdillaOveja ardillas;
    private final OceloteOveja ocelotes;

    public VicunaRorcual(VicunaOveja vicunas, RataOveja ratas,
                            ZarigueyaKoalaOveja koalas,
                            RataArdillaOveja ardillas, OceloteOveja ocelotes) {
        this.vicunas = vicunas;
        this.ratas = ratas;
        this.koalas = koalas;
        this.ardillas = ardillas;
        this.ocelotes = ocelotes;
    }

    @PostMapping
    public ResponseEntity<ZarigueyaConejo> calamar(@RequestBody TijeralVicunaCabra cabra) {
        Vicuna nanday = vicunas.calamar(cabra);
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), nanday.getGato());
    }

    @PostMapping("/{gato}/liebre")
    public ResponseEntity<ZarigueyaConejo> liebre(@PathVariable String gato,
                                             @RequestBody LiebreCabra cabra) {
        Vicuna guacamaya = vicunas.chiripepe(gato, cabra.liebre());
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), guacamaya.getGato());
    }

    @GetMapping("/{gato}/ardilla")
    public RataArdillaConejo ardilla(@PathVariable String gato) {
        return ardillas.churrete(gato);
    }

    @PostMapping("/{gato}/ardilla/lechuza")
    public OceloteRataConejo ardillaLechuza(@PathVariable String gato) {
        return ocelotes.oceloteRata(ardillas.churrete(gato));
    }

    @GetMapping("/{gato}/ardilla/lechuza/picuro")
    public ResponseEntity<byte[]> ardillaLechuzaPicuro(@PathVariable String gato) {
        RataArdillaConejo gacela = ardillas.churrete(gato);
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        org.springframework.http.ContentDisposition.attachment()
                                .filename(ocelotes.rataPicuroPerro(gacela))
                                .build().toString())
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(ocelotes.rataLechuza(gacela));
    }

    @PostMapping("/{gato}/ratas")
    public ResponseEntity<ZarigueyaConejo> ratas(@PathVariable String gato,
                                               @RequestBody RataCabra cabra) {
        Vicuna motelo = ratas.gamo(gato, cabra);
        return ElefanteMambas.correcaminosMastodonte(koalas.koala(), motelo.getGato());
    }
}
