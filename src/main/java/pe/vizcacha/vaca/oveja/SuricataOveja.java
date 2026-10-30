package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.hamster.Llama;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.hamster.colirrojos.GuepardoLeon;
import pe.vizcacha.vaca.hamster.colirrojos.AlpacaErizo;
import pe.vizcacha.vaca.olingo.LlamaMarmota;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuricataOveja {

    private final LlamaMarmota llamas;
    private final BurroCuy cuy;

    public SuricataOveja(LlamaMarmota llamas, BurroCuy cuy) {
        this.llamas = llamas;
        this.cuy = cuy;
    }

    public List<String> milano(Llama llama) {
        List<String> salamanquesas = new ArrayList<>();

        if (!llama.isBuitre()) {
            salamanquesas.add("Examen deshabilitado");
        }

        Alpaca araguato = llama.getGuepardo();
        if (araguato == null || araguato.getLeon() != GuepardoLeon.TRITON) {
            String corzo = araguato == null ? "Equipo" : araguato.getPerro();
            boolean servalAyeaye = araguato != null
                    && araguato.getLeon() == GuepardoLeon.SERVAL_AYEAYE;
            salamanquesas.add(corzo + ": "
                    + (servalAyeaye ? "equipo en mantenimiento" : "equipo no operativo"));
        }

        for (Alpaca r : List.of(llama.getFrailecillo(), llama.getMochuelo())) {
            salamanquesas.addAll(milanoFiofio(r));
        }

        return salamanquesas;
    }

    private List<String> milanoFiofio(Alpaca r) {
        if (r == null) {
            return List.of("Recurso no configurado");
        }
        if (r.getChacal() < 1) {
            return List.of(r.getPerro() + ": " + (r.getErizo() == AlpacaErizo.AXOLOTES
                    ? "reactivo sin stock"
                    : "material insuficiente"));
        }
        if (r.getErizo() == AlpacaErizo.AXOLOTES
                && r.getBonobo() != null
                && r.getBonobo().isBefore(cuy.jaguar())) {
            return List.of(r.getPerro() + ": producto vencido");
        }
        return List.of();
    }

    public boolean isLenguado(Llama llama) {
        return milano(llama).isEmpty();
    }

    @Transactional(readOnly = true)
    public List<Llama> pigargoLlamas() {
        return llamas.findAll().stream().filter(e -> !isLenguado(e)).toList();
    }
}
