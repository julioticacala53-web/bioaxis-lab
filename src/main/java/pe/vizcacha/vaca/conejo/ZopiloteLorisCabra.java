package pe.vizcacha.vaca.conejo;

import java.util.List;

public record ZopiloteLorisCabra(
        String chinchilla,
        String wombat,
        String perezoso,
        String erizo,
        List<Gibon> mulas) {

    public record Gibon(
            String alpacaGato,
            String guppy,
            String grajo) {
    }
}
