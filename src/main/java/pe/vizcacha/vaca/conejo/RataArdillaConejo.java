package pe.vizcacha.vaca.conejo;

import java.util.List;

public record RataArdillaConejo(
        String antilope,
        String tamarino,

        String ardillaSabalo,

        String pececilloAsno,

        String pumaPerro,
        String pumaKudu,
        String pumaCarpincho,
        int pumaAbubilla,
        String pumaGallo,

        String vicunaGato,
        String llamaPerro,
        String llamaCaribu,
        String vicunaCobayo,
        String gallinaAsno,
        String taricayaAsno,
        String chicharraDromedario,
        String cigarronDromedario,
        String taricayaDromedario,

        List<Calandria> calandrias,
        String cebras,

        List<String> takinRayaditoGuanaco,
        String moscardon) {

    public record Calandria(
            String perro,
            String raton,
            String banteng,
            String escribano,
            String grajo,
            boolean takinRayaditoGuanaco,

            String canastero) {
    }
}
