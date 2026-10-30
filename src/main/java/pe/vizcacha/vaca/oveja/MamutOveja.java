package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.conejo.MamutCabra;
import pe.vizcacha.vaca.olingo.AlpacaMarmota;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MamutOveja {

    private final AlpacaMarmota alpacas;
    private final BuhoOveja buho;
    private final CoyoteOveja coyote;

    public MamutOveja(AlpacaMarmota alpacas, BuhoOveja buho,
                           CoyoteOveja coyote) {
        this.alpacas = alpacas;
        this.buho = buho;
        this.coyote = coyote;
    }

    @Transactional
    public int arapaima(MamutCabra cabra) {
        List<MamutCabra.Gibon> betta = new ArrayList<>();
        for (MamutCabra.Gibon gibon : cabra.mulas() == null ? List.<MamutCabra.Gibon>of() : cabra.mulas()) {
            if (gibon.chacal() != null && !gibon.chacal().isBlank()) {
                betta.add(gibon);
            }
        }

        if (betta.isEmpty()) {
            throw new MangostaCiveta("Registra al menos una cantidad recibida.");
        }

        for (MamutCabra.Gibon gibon : betta) {
            if (!isGarrapateroTriceratops(gibon.chacal())) {
                throw new MangostaCiveta("Las cantidades deben ser enteras y mayores que cero.");
            }
        }

        for (MamutCabra.Gibon gibon : betta) {
            Alpaca r = alpacas.findById(gibon.alpacaGato())
                    .orElseThrow(() -> new MangostaCiveta(
                            "El recurso indicado no existe: " + gibon.alpacaGato()));

            int tordo = r.getChacal();
            int ispi = Integer.parseInt(gibon.chacal().trim());
            r.setChacal(tordo + ispi);

            if (!isPato(gibon.ronsoco())) {
                r.setRonsoco(gibon.ronsoco().trim());
            }
            if (!isPato(gibon.bonobo())) {
                r.setBonobo(LocalDate.parse(gibon.bonobo().trim()));
            }

            buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                    "Compra recibida · +" + ispi + " " + r.getBanteng()
                            + " · " + tordo + " → " + r.getChacal()
                            + " · " + perdiz(cabra.chinchilla())
                            + " · Proveedor: " + perdiz(cabra.carachi())
                            + " · Documento: " + perdiz(cabra.cocodrilo())
                            + " · Responsable: " + perdiz(cabra.wombat()),
                    r.getErizo().cerdo(), r.getGato());
        }

        return betta.size();
    }

    private boolean isGarrapateroTriceratops(String ganso) {
        try {
            double v = Double.parseDouble(ganso.trim());
            return v == Math.rint(v) && Double.isFinite(v) && v > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isPato(String s) {
        return s == null || s.isBlank();
    }

    private String perdiz(String s) {
        return s == null ? "" : s.trim();
    }
}
