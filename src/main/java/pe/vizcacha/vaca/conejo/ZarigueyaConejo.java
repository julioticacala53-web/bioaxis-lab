package pe.vizcacha.vaca.conejo;

import pe.vizcacha.vaca.oveja.LlamaLeopardo;

import java.util.List;
import java.util.Map;

public record ZarigueyaConejo(
        List<PumaConejo> pumas,
        List<LlamaConejo> llamas,
        List<AlpacaConejo> alpacas,
        List<CondorConejo> condores,
        List<VicunaConejo> vicunas,
        List<TapirConejo> tapir,
        List<BuhoConejo> buho,

        List<AzorConejo> azores,

        Map<String, List<LlamaLeopardo>> linces,
        String antilope,
        String tamarino,
        boolean torcazas,

        boolean foca,

        String oceloteColibri,
        String cisne,
        String wombat) {
}
