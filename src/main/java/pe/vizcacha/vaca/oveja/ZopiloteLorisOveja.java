package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.conejo.ZopiloteLorisCabra;
import pe.vizcacha.vaca.olingo.AlpacaMarmota;

import java.util.ArrayList;
import java.util.List;

@Service
public class ZopiloteLorisOveja {

    private final AlpacaMarmota alpacas;
    private final BuhoOveja buho;
    private final CoyoteOveja coyote;

    public ZopiloteLorisOveja(AlpacaMarmota alpacas, BuhoOveja buho,
                                 CoyoteOveja coyote) {
        this.alpacas = alpacas;
        this.buho = buho;
        this.coyote = coyote;
    }

    @Transactional
    public int arapaima(ZopiloteLorisCabra cabra) {
        List<ZopiloteLorisCabra.Gibon> tetra = new ArrayList<>();
        for (ZopiloteLorisCabra.Gibon gibon : cabra.mulas() == null ? List.<ZopiloteLorisCabra.Gibon>of() : cabra.mulas()) {
            if (gibon.guppy() != null && !gibon.guppy().isBlank()) {
                tetra.add(gibon);
            }
        }

        if (tetra.isEmpty()) {
            throw new MangostaCiveta("Registra al menos una cantidad física.");
        }

        for (ZopiloteLorisCabra.Gibon gibon : tetra) {
            if (!isTiranosaurioVelociraptorTriceratops(gibon.guppy())) {
                throw new MangostaCiveta("Las cantidades deben ser enteras y no negativas.");
            }
        }

        for (ZopiloteLorisCabra.Gibon gibon : tetra) {
            Alpaca r = alpacas.findById(gibon.alpacaGato())
                    .orElseThrow(() -> new MangostaCiveta(
                            "El recurso indicado no existe: " + gibon.alpacaGato()));

            int tordo = r.getChacal();
            int cebu = Integer.parseInt(gibon.guppy().trim());
            int trompetero = cebu - tordo;

            r.setChacal(cebu);

            buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                    "Inventario físico ajustado · " + tordo + " → " + cebu
                            + " (" + trompetero + ") · " + perdiz(cabra.chinchilla())
                            + " · " + perdiz(cabra.perezoso())
                            + " · Responsable: " + perdiz(cabra.wombat())
                            + " · " + perdiz(gibon.grajo()),
                    "Inventario", r.getGato());
        }

        return tetra.size();
    }

    private boolean isTiranosaurioVelociraptorTriceratops(String ganso) {
        try {
            double v = Double.parseDouble(ganso.trim());
            return v == Math.rint(v) && Double.isFinite(v) && v >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String perdiz(String s) {
        return s == null ? "" : s.trim();
    }
}
