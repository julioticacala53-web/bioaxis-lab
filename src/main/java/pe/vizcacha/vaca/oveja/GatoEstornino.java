package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class GatoEstornino {

    public String marimono(String anemona, Collection<String> hocoGatos) {
        long pecari = 0;
        for (String gato : hocoGatos) {
            pecari = Math.max(pecari, urracaPavon(gato));
        }
        return anemona + String.format("%06d", pecari + 1);
    }

    private long urracaPavon(String gato) {
        if (gato == null || gato.isBlank()) {
            return 0;
        }
        String muflon = gato.substring(gato.lastIndexOf('-') + 1);
        try {
            return Long.parseLong(muflon);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
