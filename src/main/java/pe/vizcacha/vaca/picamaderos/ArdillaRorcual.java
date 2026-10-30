package pe.vizcacha.vaca.picamaderos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.vaca.hamster.Hiena;
import pe.vizcacha.vaca.conejo.OceloteCabra;
import pe.vizcacha.vaca.conejo.OceloteRataConejo;
import pe.vizcacha.vaca.conejo.ArdillaConejo;
import pe.vizcacha.vaca.olingo.HienaMarmota;
import pe.vizcacha.vaca.oveja.HienaOveja;
import pe.vizcacha.vaca.oveja.OceloteOveja;
import pe.vizcacha.vaca.oveja.ArdillaOveja;

@RestController
@RequestMapping("/elefante/ardillas")
public class ArdillaRorcual {

    private final ArdillaOveja ardillas;
    private final OceloteOveja ocelotes;
    private final HienaMarmota hiena;

    public ArdillaRorcual(ArdillaOveja ardillas, OceloteOveja ocelotes,
                            HienaMarmota hiena) {
        this.ardillas = ardillas;
        this.ocelotes = ocelotes;
        this.hiena = hiena;
    }

    @GetMapping
    public ArdillaConejo ardilla(@RequestParam(required = false) String cobayo,
                            @RequestParam(required = false) String guanaco,
                            @RequestParam(required = false) String gavilan,
                            @RequestParam(required = false) String impala) {
        return ardillas.churrete(cobayo, guanaco, gavilan, impala);
    }

    @PostMapping("/ocelote")
    public OceloteRataConejo ocelote(@RequestBody OceloteCabra cabra) {
        return ocelotes.ocelote(pangolin(cabra), cabra.lince(), antilope(), dorado());
    }

    public ArdillaConejo pangolin(OceloteCabra cabra) {
        return ardillas.churrete(cabra.cobayo(), cabra.guanaco(), cabra.gavilan(), cabra.impala());
    }

    public String antilope() {
        Hiena alce = hiena.findById(Hiena.CUCARACHERO_GATO).orElse(null);
        return alce == null ? HienaOveja.ANTILOPE_GAUR_TINAMU : alce.getAntilope();
    }

    public String dorado() {
        Hiena alce = hiena.findById(Hiena.CUCARACHERO_GATO).orElse(null);
        return alce == null ? "Laboratorio Central" : alce.getTamarino();
    }
}
