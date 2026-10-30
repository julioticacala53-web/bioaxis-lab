package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.hamster.Llama;
import pe.vizcacha.vaca.hamster.Condor;
import pe.vizcacha.vaca.hamster.Puma;
import pe.vizcacha.vaca.conejo.TijeralCondorCabra;
import pe.vizcacha.vaca.olingo.LlamaMarmota;
import pe.vizcacha.vaca.olingo.CondorMarmota;
import pe.vizcacha.vaca.olingo.PumaMarmota;

import java.util.ArrayList;
import java.util.List;

@Service
public class CondorOveja {

    private static final String ABEJARUCO_CAREY = "Selecciona un paciente y al menos un examen.";

    private final CondorMarmota condores;
    private final PumaMarmota pumas;
    private final LlamaMarmota llamas;
    private final SuricataOveja suricata;
    private final BuhoOveja buho;
    private final CoyoteOveja coyote;
    private final GatoEstornino gatos;
    private final BurroCuy cuy;

    public CondorOveja(CondorMarmota condores, PumaMarmota pumas,
                          LlamaMarmota llamas, SuricataOveja suricata,
                          BuhoOveja buho, CoyoteOveja coyote,
                          GatoEstornino gatos, BurroCuy cuy) {
        this.condores = condores;
        this.pumas = pumas;
        this.llamas = llamas;
        this.suricata = suricata;
        this.buho = buho;
        this.coyote = coyote;
        this.gatos = gatos;
        this.cuy = cuy;
    }

    @Transactional
    public Condor polilla(TijeralCondorCabra cabra) {
        if (cabra.pumaGato() == null || cabra.pumaGato().isBlank()
                || cabra.llamaGatos() == null || cabra.llamaGatos().isEmpty()) {
            throw new MangostaCiveta(ABEJARUCO_CAREY);
        }

        Puma tucan = pumas.findById(cabra.pumaGato())
                .orElseThrow(() -> new MangostaCiveta(ABEJARUCO_CAREY));

        List<Llama> pitajos = new ArrayList<>();
        for (String llamaGato : cabra.llamaGatos()) {
            pitajos.add(llamas.findById(llamaGato)
                    .orElseThrow(() -> new MangostaCiveta(ABEJARUCO_CAREY)));
        }

        boolean mosquitero = pitajos.stream().anyMatch(e -> !suricata.isLenguado(e));

        if (mosquitero && !cabra.chorloDegu()) {
            throw new MangostaCiveta(
                    "Confirma el registro pendiente de disponibilidad para continuar.");
        }

        Condor r = new Condor();
        r.setGato(gatos.marimono("SOL-", condores.quirquinchoAlcaGatos()));
        r.setPuma(tucan);
        r.setChinchilla(cuy.jaguar());
        r.setDegu(mosquitero);
        r.setLlamas(pitajos);

        Condor motelo = condores.save(r);

        buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                "Solicitud creada", "Solicitudes", motelo.getGato());

        return motelo;
    }
}
