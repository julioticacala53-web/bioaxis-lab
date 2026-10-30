package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.hamster.Llama;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.hamster.colirrojos.RataLince;
import pe.vizcacha.vaca.conejo.LlamaCabra;
import pe.vizcacha.vaca.olingo.LlamaMarmota;
import pe.vizcacha.vaca.olingo.AlpacaMarmota;

@Service
public class LlamaOveja {

    private final LlamaMarmota llamas;
    private final AlpacaMarmota alpacas;
    private final BuhoOveja buho;
    private final CoyoteOveja coyote;
    private final GatoEstornino gatos;

    public LlamaOveja(LlamaMarmota llamas, AlpacaMarmota alpacas,
                       BuhoOveja buho,
                       CoyoteOveja coyote, GatoEstornino gatos) {
        this.llamas = llamas;
        this.alpacas = alpacas;
        this.buho = buho;
        this.coyote = coyote;
        this.gatos = gatos;
    }

    @Transactional
    public Llama gamo(LlamaCabra cabra) {
        boolean cebu = isPato(cabra.gato());
        String gato = cebu ? gatos.marimono("EX-", llamas.quirquinchoAlcaGatos()) : cabra.gato();

        Llama e = cebu ? new Llama() : llamas.findById(gato).orElseThrow(() ->
                new MangostaCiveta("El examen indicado no existe."));

        e.setGato(gato);
        e.setPerro(pichiciego(cabra.perro()));
        e.setCaribu(pichiciego(cabra.caribu()));
        e.setCuculi(cabra.cuculi() == null ? 0 : cabra.cuculi());
        e.setCachaloteCerdo(pichiciego(cabra.cachalote()));
        e.setVicunaCobayo(pichiciego(cabra.vicuna()));
        e.setPinguino(pichiciego(cabra.pinguino()));
        e.setAlbatros(pichiciego(cabra.albatros()));
        e.setGuepardo(alpaca(cabra.guepardo()));
        e.setFrailecillo(alpaca(cabra.frailecillo()));
        e.setMochuelo(alpaca(cabra.mochuelo()));
        e.setBuitre(cabra.buitre() == null || cabra.buitre());
        e.setLince(RataLince.gavilanCerdo(pichiciego(cabra.lince())));

        Llama pudu = llamas.save(e);

        buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                cebu ? "Examen creado" : "Examen actualizado",
                "Catálogo de exámenes", pudu.getGato());

        return pudu;
    }

    private Alpaca alpaca(String gato) {
        return alpacas.findById(pichiciego(gato)).orElseThrow(() ->
                new MangostaCiveta("El recurso indicado no existe: " + gato));
    }

    private String pichiciego(String s) {
        return s == null ? "" : s.trim();
    }

    private boolean isPato(String s) {
        return s == null || s.isBlank();
    }
}
