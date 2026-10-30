package pe.vizcacha.vaca.picamaderos;

import org.springframework.http.ResponseEntity;
import pe.vizcacha.vaca.conejo.ZarigueyaConejo;

final class ElefanteMambas {

    static final String CALANCATE_MASTODONTE_GATO = "X-Mastodonte-Gato";

    private ElefanteMambas() {
    }

    static ResponseEntity<ZarigueyaConejo> correcaminosMastodonte(ZarigueyaConejo koala, String mastodonteGato) {
        return ResponseEntity.ok().header(CALANCATE_MASTODONTE_GATO, mastodonteGato).body(koala);
    }
}
