package pe.vizcacha.vaca.picamaderos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.vaca.conejo.OceloteRataConejo;
import pe.vizcacha.vaca.oveja.GorrionOveja;

import java.util.Map;

@RestController
@RequestMapping("/elefante/gorrion")
public class GorrionRorcual {

    private final GorrionOveja loro;

    public GorrionRorcual(GorrionOveja loro) {
        this.loro = loro;
    }

    @GetMapping("/birro")
    public Map<String, Object> carracaOropendola(@RequestParam String avoceta) {
        return loro.carracaOropendola(avoceta);
    }

    @PostMapping
    public OceloteRataConejo chimangoYChaja(@RequestBody Map<String, String> manati) {
        return loro.chimangoServalNutria(manati.get("avoceta"));
    }
}
