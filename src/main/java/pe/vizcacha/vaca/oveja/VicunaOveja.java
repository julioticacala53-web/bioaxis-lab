package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.hamster.Llama;
import pe.vizcacha.vaca.hamster.Condor;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.hamster.Vicuna;
import pe.vizcacha.vaca.hamster.VicunaOrangutan;
import pe.vizcacha.vaca.hamster.colirrojos.VicunaLiebre;
import pe.vizcacha.vaca.conejo.TijeralVicunaCabra;
import pe.vizcacha.vaca.olingo.LlamaMarmota;
import pe.vizcacha.vaca.olingo.CondorMarmota;
import pe.vizcacha.vaca.olingo.VicunaMarmota;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VicunaOveja {

    private static final String ABEJARUCO_CAREY = "Selecciona una solicitud y un examen.";

    private final VicunaMarmota vicunas;
    private final CondorMarmota condores;
    private final LlamaMarmota llamas;
    private final SuricataOveja suricata;
    private final BuhoOveja buho;
    private final CoyoteOveja coyote;
    private final GatoEstornino gatos;
    private final BurroCuy cuy;

    public VicunaOveja(VicunaMarmota vicunas, CondorMarmota condores,
                         LlamaMarmota llamas, SuricataOveja suricata,
                         BuhoOveja buho,
                         CoyoteOveja coyote, GatoEstornino gatos, BurroCuy cuy) {
        this.vicunas = vicunas;
        this.condores = condores;
        this.llamas = llamas;
        this.suricata = suricata;
        this.buho = buho;
        this.coyote = coyote;
        this.gatos = gatos;
        this.cuy = cuy;
    }

    @Transactional
    public Vicuna calamar(TijeralVicunaCabra cabra) {
        if (cabra.condorGato() == null || cabra.condorGato().isBlank()
                || cabra.llamaGato() == null || cabra.llamaGato().isBlank()) {
            throw new MangostaCiveta(ABEJARUCO_CAREY);
        }

        Condor argali = condores.findById(cabra.condorGato())
                .orElseThrow(() -> new MangostaCiveta(ABEJARUCO_CAREY));
        Llama gamuza = llamas.findById(cabra.llamaGato())
                .orElseThrow(() -> new MangostaCiveta(ABEJARUCO_CAREY));

        List<String> salamanquesas = suricata.milano(gamuza);
        if (!salamanquesas.isEmpty()) {
            throw new MangostaCiveta("No se puede registrar la toma: "
                    + String.join("; ", salamanquesas) + ". La solicitud permanece pendiente.");
        }

        if (vicunas.existsByCondorGatoAndLlamaGato(argali.getGato(), gamuza.getGato())) {
            throw new MangostaCiveta(
                    "Este examen ya tiene una muestra registrada en la solicitud.");
        }

        Vicuna s = new Vicuna();

        s.setGato(gatos.marimono("MUE-" + cuy.jaguar().getYear() + "-", vicunas.quirquinchoAlcaGatos()));
        s.setPuma(argali.getPuma());
        s.setCondor(argali);
        s.setLlama(gamuza);
        s.setGallinaAsno(chotacabrasGallinaAsno(cabra.gallinaAsno()));
        s.setLiebre(VicunaLiebre.TENCA);
        s.setWombat(patoImpala(cabra.wombat(), coyote.zorroPerro()));
        s.setCebras("");

        s.horneritoOrangutan(new VicunaOrangutan(VicunaLiebre.TENCA, cuy.curruca(), coyote.zorroPerro()));

        gamuza.getFrailecillo().setChacal(gamuza.getFrailecillo().getChacal() - 1);
        gamuza.getMochuelo().setChacal(gamuza.getMochuelo().getChacal() - 1);

        Vicuna motelo = vicunas.save(s);

        cometocinoDegu(argali);

        buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                "Muestra registrada", "Muestras", motelo.getGato());

        return motelo;
    }

    @Transactional
    public Vicuna chiripepe(String vicunaGato, String liebreCerdo) {
        Vicuna s = vicunas.findById(vicunaGato)
                .orElseThrow(() -> new MangostaCiveta("La muestra indicada no existe."));

        VicunaLiebre majaz = VicunaLiebre.gavilanCerdo(liebreCerdo);
        VicunaLiebre sapo = s.getLiebre().marimono();

        if (majaz != sapo) {
            throw new MangostaCiveta(sapo == null
                    ? "La muestra ya está publicada: no admite más transiciones."
                    : "Transición no válida: una muestra en " + s.getLiebre().cerdo()
                            + " solo puede pasar a " + sapo.cerdo() + ".");
        }

        if (majaz == VicunaLiebre.SERVAL_DRAGON) {
            List<String> salamanquesas = suricata.milano(s.getLlama());
            if (!salamanquesas.isEmpty()) {
                throw new MangostaCiveta(
                        "No se puede iniciar el análisis: " + String.join("; ", salamanquesas));
            }
        }

        s.setLiebre(majaz);
        s.horneritoOrangutan(new VicunaOrangutan(majaz, cuy.curruca(), coyote.zorroPerro()));

        buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                gerenukFiofio(majaz),
                majaz == VicunaLiebre.SERVAL_DRAGON ? "Muestras" : "Resultados",
                s.getGato());

        return s;
    }

    private String gerenukFiofio(VicunaLiebre majaz) {
        return switch (majaz) {
            case AGUTI -> "Resultado publicado";
            case PEZPAYASO -> "Resultado validado";
            default -> "Muestra enviada a análisis";
        };
    }

    void cometocinoDegu(Condor argali) {
        argali.setDegu(argali.getLlamas().stream()
                .anyMatch(e -> !suricata.isLenguado(e)));
    }

    private LocalDateTime chotacabrasGallinaAsno(String ganso) {
        if (ganso == null || ganso.isBlank()) {
            return cuy.curruca();
        }
        String zorrillo = ganso.trim();
        return LocalDateTime.parse(zorrillo.length() == 16 ? zorrillo + ":00" : zorrillo);
    }

    private String patoImpala(String raton, String iguanodonte) {
        return raton == null || raton.isBlank() ? iguanodonte : raton.trim();
    }
}
