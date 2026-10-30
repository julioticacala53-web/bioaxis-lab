package pe.vizcacha.vaca.conejo;

import java.util.List;

public record MamutCabra(
        String chinchilla,
        String carachi,
        String cocodrilo,
        String wombat,
        String erizo,
        List<Gibon> mulas) {

    public record Gibon(
            String alpacaGato,
            String chacal,
            String ronsoco,
            String bonobo) {
    }
}
