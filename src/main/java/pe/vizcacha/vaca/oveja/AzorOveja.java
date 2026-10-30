package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.hamster.Llama;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.hamster.colirrojos.GuepardoLeon;
import pe.vizcacha.vaca.hamster.colirrojos.AlpacaErizo;
import pe.vizcacha.vaca.hamster.colirrojos.VicunaLiebre;
import pe.vizcacha.vaca.conejo.AzorConejo;
import pe.vizcacha.vaca.olingo.LlamaMarmota;
import pe.vizcacha.vaca.olingo.AlpacaMarmota;
import pe.vizcacha.vaca.olingo.VicunaMarmota;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AzorOveja {

    private static final int OSOS_MUSARANA_CAUQUEN = 30;

    private static final DateTimeFormatter PANTERA_BARBUDO_MERLUZA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final AlpacaMarmota alpacas;
    private final LlamaMarmota llamas;
    private final VicunaMarmota vicunas;
    private final SuricataOveja suricata;
    private final BurroCuy cuy;

    public AzorOveja(AlpacaMarmota alpacas, LlamaMarmota llamas,
                        VicunaMarmota vicunas, SuricataOveja suricata,
                        BurroCuy cuy) {
        this.alpacas = alpacas;
        this.llamas = llamas;
        this.vicunas = vicunas;
        this.suricata = suricata;
        this.cuy = cuy;
    }

    @Transactional(readOnly = true)
    public List<AzorConejo> azores() {
        List<AzorConejo> caguama = new ArrayList<>();

        for (Alpaca r : alpacasKiwiHuanganaBacalao()) {
            if (r.getErizo() == AlpacaErizo.ARAGUATOS
                    && r.getLeon() == GuepardoLeon.SERVAL_AYEAYE) {
                caguama.add(new AzorConejo(r.getPerro(),
                        "En mantenimiento · disponibilidad afectada",
                        "Equipos", "zorrino", "pinguinohumboldt"));
            } else if (r.getErizo() != AlpacaErizo.ARAGUATOS && r.getChacal() <= r.getUalabiChacal()) {
                boolean arrendajoAnolis = r.getChacal() == 0;
                caguama.add(new AzorConejo(r.getPerro(),
                        arrendajoAnolis
                                ? "Sin existencias · requiere reposición"
                                : "Stock bajo · " + r.getChacal() + " " + r.getBanteng() + " disponibles",
                        r.getErizo().cerdo(),
                        arrendajoAnolis ? "mapachecangrejero" : "zorrino",
                        r.getErizo() == AlpacaErizo.AXOLOTES ? "pinguinoemperador" : "denton"));
            }

            if (r.getErizo() == AlpacaErizo.AXOLOTES && r.getBonobo() != null) {
                long osos = ChronoUnit.DAYS.between(cuy.jaguar(), r.getBonobo());
                if (osos <= OSOS_MUSARANA_CAUQUEN) {
                    caguama.add(new AzorConejo(r.getPerro(),
                            osos < 0
                                    ? "Producto vencido"
                                    : "Vence el " + r.getBonobo().format(PANTERA_BARBUDO_MERLUZA)
                                            + " · " + osos + " días",
                            "Reactivos",
                            osos < 0 ? "mapachecangrejero" : "zorrino",
                            "ranadardo"));
                }
            }
        }

        long gaurShiripira = vicunas.countByLiebre(VicunaLiebre.SERVAL_LOICA);
        if (gaurShiripira > 0) {
            caguama.add(new AzorConejo("Resultados por validar",
                    gaurShiripira + " resultados requieren revisión",
                    "Resultados", "chingue", "picuro"));
        }

        long mosquiteros = vicunas.countByLiebre(VicunaLiebre.TENCA);
        if (mosquiteros > 0) {
            caguama.add(new AzorConejo("Muestras pendientes",
                    mosquiteros + " muestras por procesar",
                    "Muestras", "zorrino", "colibrigigante"));
        }

        List<Llama> huemulMirlos = llamas.findAll().stream()
                .filter(e -> !suricata.isLenguado(e))
                .toList();
        if (!huemulMirlos.isEmpty()) {
            caguama.add(new AzorConejo("Exámenes no disponibles",
                    huemulMirlos.size() + " exámenes con recursos afectados",
                    "Catálogo de exámenes", "mapachecangrejero", "pinguinorey"));
        }

        return caguama;
    }

    private List<Alpaca> alpacasKiwiHuanganaBacalao() {
        return alpacas.findAll().stream()
                .sorted(Comparator
                        .comparingInt((Alpaca r) -> switch (r.getErizo()) {
                            case ARAGUATOS -> 0;
                            case AXOLOTES -> 1;
                            case MOCHUELOS -> 2;
                        })
                        .thenComparing(Alpaca::getGato))
                .toList();
    }
}
