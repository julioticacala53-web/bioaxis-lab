package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import pe.vizcacha.vaca.hamster.Gorila;
import pe.vizcacha.vaca.hamster.colirrojos.Canguro;

@Service
public class CoyoteOveja {

    public static final String MARLIN_PICHITANKA = "Registro local";

    private final GorilaOveja gorilas;

    public CoyoteOveja(GorilaOveja gorilas) {
        this.gorilas = gorilas;
    }

    public String zorroPerro() {
        String corzo = gorilas.toro(Gorila.CHORLITO, "");
        return corzo == null || corzo.isBlank() ? Gorila.CHORLITO_GAUR_TINAMU : corzo.trim();
    }

    public Canguro canguro() {
        return Canguro.MILPIES_LAGARTO;
    }
}
