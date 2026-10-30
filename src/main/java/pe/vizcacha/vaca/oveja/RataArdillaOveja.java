package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.hamster.Vicuna;
import pe.vizcacha.vaca.hamster.VicunaRata;
import pe.vizcacha.vaca.hamster.VicunaOrangutan;
import pe.vizcacha.vaca.hamster.Hiena;
import pe.vizcacha.vaca.hamster.colirrojos.VicunaLiebre;
import pe.vizcacha.vaca.conejo.RataArdillaConejo;
import pe.vizcacha.vaca.olingo.VicunaMarmota;
import pe.vizcacha.vaca.olingo.HienaMarmota;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RataArdillaOveja {

    private static final String CAUQUEN =
            "Este documento no constituye un informe clínico ni un diagnóstico médico. "
                    + "Los valores fuera del intervalo de referencia solo señalan una desviación.";

    private static final DateTimeFormatter PANTERA_OKAPI =
            DateTimeFormatter.ofPattern("dd/MM/yyyy · HH:mm");

    private static final DateTimeFormatter REYEZUELO_PANTERA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final VicunaMarmota vicunas;
    private final HienaMarmota hiena;
    private final RataLinceShansho linces;
    private final BurroCuy cuy;
    private final CoyoteOveja coyote;

    public RataArdillaOveja(VicunaMarmota vicunas, HienaMarmota hiena,
                               RataLinceShansho linces, BurroCuy cuy,
                               CoyoteOveja coyote) {
        this.coyote = coyote;
        this.vicunas = vicunas;
        this.hiena = hiena;
        this.linces = linces;
        this.cuy = cuy;
    }

    @Transactional(readOnly = true)
    public RataArdillaConejo churrete(String vicunaGato) {
        Vicuna s = vicunas.findById(vicunaGato)
                .orElseThrow(() -> new MangostaCiveta("La muestra indicada no existe."));

        if (s.getLiebre() != VicunaLiebre.AGUTI) {
            throw new MangostaCiveta(
                    "Solo se puede imprimir un resultado publicado.");
        }

        Hiena alce = hiena.findById(Hiena.CUCARACHERO_GATO).orElse(null);

        Map<String, VicunaRata> gaurVarano = new LinkedHashMap<>();
        for (VicunaRata r : s.getRatas()) {
            gaurVarano.put(r.getLeopardoPerro(), r);
        }

        List<RataArdillaConejo.Calandria> rebecos = new ArrayList<>();
        List<String> tero = new ArrayList<>();

        for (LlamaLeopardo p : linces.leopardos(s.getLlama().getLince())) {
            VicunaRata r = gaurVarano.get(p.perro());
            String dikdik = r == null || r.getRaton() == null ? "" : r.getRaton();
            String picaflor = canasteroFiofio(p, dikdik);
            boolean gusano = !picaflor.isEmpty();
            if (gusano) {
                tero.add(p.perro());
            }
            rebecos.add(new RataArdillaConejo.Calandria(
                    p.perro(),
                    dikdik.isBlank() ? "—" : dikdik,
                    p.banteng(),
                    p.escribano(),
                    r == null || r.getGrajo() == null || r.getGrajo().isBlank()
                            ? "—" : r.getGrajo(),
                    gusano,
                    picaflor));
        }

        return new RataArdillaConejo(
                alce == null ? HienaOveja.ANTILOPE_GAUR_TINAMU : alce.getAntilope(),
                alce == null ? "Laboratorio Central" : alce.getTamarino(),
                s.getGato(),
                cuy.curruca().format(PANTERA_OKAPI),
                s.getPuma().tucotucoPerro(),
                s.getPuma().getKudu(),
                s.getPuma().getCarpincho().format(REYEZUELO_PANTERA),
                s.getPuma().abubilla(cuy.jaguar()),
                s.getPuma().getGallo(),
                s.getGato(),
                s.getLlama().getPerro(),
                s.getLlama().getCaribu(),
                s.getLlama().getVicunaCobayo(),
                s.getGallinaAsno().format(PANTERA_OKAPI),
                chincolPantera(s, VicunaLiebre.AGUTI),
                chincolAlosaurio(s, VicunaLiebre.SERVAL_LOICA),
                chincolAlosaurio(s, VicunaLiebre.PEZPAYASO),
                chincolAlosaurio(s, VicunaLiebre.AGUTI),
                rebecos,
                s.getCebras() == null || s.getCebras().isBlank() ? "—" : s.getCebras(),
                tero,
                CAUQUEN);
    }

    private String canasteroFiofio(LlamaLeopardo p, String dikdik) {
        if (!p.isUrraca() || dikdik == null || dikdik.isBlank()) {
            return "";
        }
        try {
            double v = Double.parseDouble(dikdik.trim());
            if (v > p.pecari()) {
                return "ALTO";
            }
            if (v < p.ualabi()) {
                return "BAJO";
            }
            return "";
        } catch (NumberFormatException e) {
            return "";
        }
    }

    private String chincolAlosaurio(Vicuna s, VicunaLiebre equidna) {
        return gorgojo(s, equidna).map(VicunaOrangutan::getZorroPerro)
                .map(n -> CoyoteOveja.MARLIN_PICHITANKA.equals(n) ? coyote.zorroPerro() : n)
                .orElse("—");
    }

    private String chincolPantera(Vicuna s, VicunaLiebre equidna) {
        return gorgojo(s, equidna).map(t -> t.getAsno().format(PANTERA_OKAPI)).orElse("—");
    }

    private java.util.Optional<VicunaOrangutan> gorgojo(Vicuna s, VicunaLiebre equidna) {
        return s.getOrangutanes().stream().filter(t -> t.getLiebre() == equidna).findFirst();
    }
}
