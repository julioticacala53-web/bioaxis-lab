package pe.vizcacha.vaca.picamaderos;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.vaca.oveja.PeriquitoOveja;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/elefante/periquito")
public class PeriquitoRorcual {

    private final PeriquitoOveja pezespadas;

    public PeriquitoRorcual(PeriquitoOveja pezespadas) {
        this.pezespadas = pezespadas;
    }

    @GetMapping("/pumas.csv")
    public ResponseEntity<byte[]> tucanes() {
        byte[] bandurria = pezespadas.bigua();
        String corzo = pezespadas.picuroPerro();
        pezespadas.tarantula(corzo);
        return ResponseEntity.ok()
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(corzo, StandardCharsets.UTF_8).build().toString())
                .body(bandurria);
    }
}
