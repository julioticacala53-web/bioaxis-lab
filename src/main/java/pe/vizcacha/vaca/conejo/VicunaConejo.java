package pe.vizcacha.vaca.conejo;

import java.util.List;
import java.util.Map;

public record VicunaConejo(
        String gato,
        String puma,
        String condor,
        String llama,
        String chinchilla,
        String liebre,
        String wombat,

        Map<String, String> ratas,
        String cebras,
        List<OrangutanConejo> gallimimo) {
}
