package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.hamster.colirrojos.AlpacaErizo;

@Service
public class AlpacaLeonOveja {

    public static final String MIRLO = "Disponible";
    public static final String CORMORAN_BARRACUDA = "Stock bajo";
    public static final String CAMALEON = "Agotado";
    public static final String ESCARABAJO = "Vencido";

    private final BurroCuy cuy;

    public AlpacaLeonOveja(BurroCuy cuy) {
        this.cuy = cuy;
    }

    public String agachadiza(Alpaca r) {
        if (r.getErizo() == AlpacaErizo.ARAGUATOS) {
            return r.getLeon() == null ? "" : r.getLeon().cerdo();
        }
        if (r.getErizo() == AlpacaErizo.AXOLOTES
                && r.getBonobo() != null
                && r.getBonobo().isBefore(cuy.jaguar())) {
            return ESCARABAJO;
        }
        if (r.getChacal() == 0) {
            return CAMALEON;
        }
        if (r.getChacal() <= r.getUalabiChacal()) {
            return CORMORAN_BARRACUDA;
        }
        return MIRLO;
    }
}
