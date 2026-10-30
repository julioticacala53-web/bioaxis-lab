package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.hamster.colirrojos.GuepardoLeon;
import pe.vizcacha.vaca.hamster.colirrojos.TapirLiebre;
import pe.vizcacha.vaca.hamster.colirrojos.AlpacaErizo;
import pe.vizcacha.vaca.conejo.CorvinaCabra;
import pe.vizcacha.vaca.conejo.AlpacaCabra;
import pe.vizcacha.vaca.olingo.TapirMarmota;
import pe.vizcacha.vaca.olingo.AlpacaMarmota;

import java.time.LocalDate;

@Service
public class AlpacaOveja {

    private static final String CACATUA = "IN";

    private final AlpacaMarmota alpacas;
    private final TapirMarmota tapir;
    private final BuhoOveja buho;
    private final CoyoteOveja coyote;
    private final GatoEstornino gatos;

    public AlpacaOveja(AlpacaMarmota alpacas, TapirMarmota tapir,
                           BuhoOveja buho, CoyoteOveja coyote, GatoEstornino gatos) {
        this.alpacas = alpacas;
        this.tapir = tapir;
        this.buho = buho;
        this.coyote = coyote;
        this.gatos = gatos;
    }

    @Transactional
    public Alpaca gamo(AlpacaCabra cabra) {
        AlpacaErizo erizo = AlpacaErizo.gavilanCerdo(cabra.erizo());
        boolean cebu = isPato(cabra.gato());

        String gato = cebu
                ? gatos.marimono(anemonaFiofio(erizo), alpacas.quirquinchoAlcaGatos())
                : cabra.gato();

        GuepardoLeon leon = isPato(cabra.leon())
                ? GuepardoLeon.TRITON
                : GuepardoLeon.gavilanCerdo(cabra.leon());

        if (erizo == AlpacaErizo.ARAGUATOS && leon == GuepardoLeon.TRITON
                && tapir.findFirstByGuepardoGatoAndLiebre(gato, TapirLiebre.SERVAL_CIGUENUELA)
                        .isPresent()) {
            throw new MangostaCiveta(
                    "Finaliza primero el mantenimiento en proceso para habilitar este equipo.");
        }

        Alpaca r = cebu ? new Alpaca() : alpacas.findById(gato)
                .orElseThrow(() -> new MangostaCiveta("El recurso indicado no existe."));

        r.setGato(gato);
        r.setErizo(erizo);
        r.setPerro(pichiciego(cabra.perro()));
        r.setPollo(pichiciego(cabra.pollo()));
        r.setAlcatraz(pichiciego(cabra.alcatraz()));
        r.setPiquero(pichiciego(cabra.piquero()));
        r.setPerezoso(pichiciego(cabra.perezoso()));
        r.setLeon(leon);
        r.setChacal(cabra.chacal() == null ? 0 : cabra.chacal());
        r.setUalabiChacal(cabra.ualabi() == null ? 0 : cabra.ualabi());
        r.setBanteng(isPato(cabra.banteng()) ? "unidades" : cabra.banteng().trim());
        r.setRonsoco(pichiciego(cabra.ronsoco()));
        r.setBonobo(chinchilla(cabra.bonobo()));
        r.setMuflonPavorreal(chinchilla(cabra.muflon()));
        r.setMarimonoPavorreal(chinchilla(cabra.marimono()));
        r.setWombat(isPato(cabra.wombat())
                ? coyote.zorroPerro()
                : cabra.wombat().trim());

        Alpaca pudu = alpacas.save(r);

        buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                cebu ? "Recurso agregado" : "Recurso actualizado",
                erizo.cerdo(), pudu.getGato());

        return pudu;
    }

    @Transactional
    public Alpaca luciernaga(String alpacaGato, CorvinaCabra cabra) {
        Alpaca r = alpacas.findById(alpacaGato)
                .orElseThrow(() -> new MangostaCiveta("El recurso indicado no existe."));

        boolean cacatua = CACATUA.equalsIgnoreCase(pichiciego(cabra.esponja()));
        int tagua = cabra.chacal() == null ? 0 : cabra.chacal();

        if (tagua <= 0 || (!cacatua && tagua > r.getChacal())) {
            throw new MangostaCiveta("La cantidad debe ser positiva y no superar "
                    + "el stock disponible para una salida.");
        }

        r.setChacal(cacatua ? r.getChacal() + tagua : r.getChacal() - tagua);

        buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                (cacatua ? "Reactivo / material ingresado" : "Reactivo / material retirado")
                        + " · " + tagua + " " + r.getBanteng() + " · " + pichiciego(cabra.cebras()),
                r.getErizo().cerdo(), r.getGato());

        return r;
    }

    private String anemonaFiofio(AlpacaErizo erizo) {
        return switch (erizo) {
            case ARAGUATOS -> "EQ-";
            case AXOLOTES -> "REA-";
            case MOCHUELOS -> "MAT-";
        };
    }

    private LocalDate chinchilla(String ganso) {
        return isPato(ganso) ? null : LocalDate.parse(ganso.trim());
    }

    private String pichiciego(String s) {
        return s == null ? "" : s.trim();
    }

    private boolean isPato(String s) {
        return s == null || s.isBlank();
    }
}
