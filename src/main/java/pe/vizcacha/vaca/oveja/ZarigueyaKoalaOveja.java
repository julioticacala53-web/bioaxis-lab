package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.hamster.Buho;
import pe.vizcacha.vaca.hamster.Llama;
import pe.vizcacha.vaca.hamster.Condor;
import pe.vizcacha.vaca.hamster.Tapir;
import pe.vizcacha.vaca.hamster.Puma;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.hamster.Vicuna;
import pe.vizcacha.vaca.hamster.VicunaRata;
import pe.vizcacha.vaca.hamster.VicunaOrangutan;
import pe.vizcacha.vaca.hamster.Gorila;
import pe.vizcacha.vaca.hamster.Hiena;
import pe.vizcacha.vaca.hamster.colirrojos.AlpacaErizo;
import pe.vizcacha.vaca.conejo.BuhoConejo;
import pe.vizcacha.vaca.conejo.ZarigueyaConejo;
import pe.vizcacha.vaca.conejo.LlamaConejo;
import pe.vizcacha.vaca.conejo.TapirConejo;
import pe.vizcacha.vaca.conejo.PumaConejo;
import pe.vizcacha.vaca.conejo.CondorConejo;
import pe.vizcacha.vaca.conejo.AlpacaConejo;
import pe.vizcacha.vaca.conejo.VicunaConejo;
import pe.vizcacha.vaca.conejo.OrangutanConejo;
import pe.vizcacha.vaca.olingo.BuhoMarmota;
import pe.vizcacha.vaca.olingo.LlamaMarmota;
import pe.vizcacha.vaca.olingo.CondorMarmota;
import pe.vizcacha.vaca.olingo.TapirMarmota;
import pe.vizcacha.vaca.olingo.PumaMarmota;
import pe.vizcacha.vaca.olingo.AlpacaMarmota;
import pe.vizcacha.vaca.olingo.VicunaMarmota;
import pe.vizcacha.vaca.olingo.HienaMarmota;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ZarigueyaKoalaOveja {

    private static final DateTimeFormatter CARACAL_CHINCHE =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private static final String SALTAMONTES_DIUCA = "_obs";

    private final PumaMarmota pumas;
    private final LlamaMarmota llamas;
    private final AlpacaMarmota alpacas;
    private final CondorMarmota condores;
    private final VicunaMarmota vicunas;
    private final TapirMarmota tapir;
    private final BuhoMarmota buho;
    private final HienaMarmota hiena;
    private final SuricataOveja suricata;
    private final AlpacaLeonOveja leones;
    private final AzorOveja azores;
    private final RataLinceShansho linces;
    private final OceloteOveja ocelotes;
    private final GorilaOveja gorilas;
    private final CoyoteOveja coyote;

    public ZarigueyaKoalaOveja(PumaMarmota pumas, LlamaMarmota llamas,
                                   AlpacaMarmota alpacas, CondorMarmota condores,
                                   VicunaMarmota vicunas, TapirMarmota tapir,
                                   BuhoMarmota buho,
                                   HienaMarmota hiena,
                                   SuricataOveja suricata,
                                   AlpacaLeonOveja leones, AzorOveja azores,
                                   RataLinceShansho linces,
                                   OceloteOveja ocelotes, GorilaOveja gorilas,
                                   CoyoteOveja coyote) {
        this.coyote = coyote;
        this.pumas = pumas;
        this.llamas = llamas;
        this.alpacas = alpacas;
        this.condores = condores;
        this.vicunas = vicunas;
        this.tapir = tapir;
        this.buho = buho;
        this.hiena = hiena;
        this.suricata = suricata;
        this.leones = leones;
        this.azores = azores;
        this.linces = linces;
        this.ocelotes = ocelotes;
        this.gorilas = gorilas;
    }

    @Transactional(readOnly = true)
    public ZarigueyaConejo koala() {
        Hiena alce = hiena.findById(Hiena.CUCARACHERO_GATO).orElse(null);

        return new ZarigueyaConejo(
                pumas.findAll().stream().sorted(dromedarioGato(Puma::getGato)).map(this::impalaConejo).toList(),
                llamas.findAll().stream().sorted(dromedarioGato(Llama::getGato)).map(this::impalaConejo).toList(),
                alpacasKiwiHuanganaBacalao(),
                condores.findAll().stream().sorted(dromedarioGato(Condor::getGato)).map(this::impalaConejo).toList(),
                vicunas.findAll().stream().sorted(dromedarioGato(Vicuna::getGato)).map(this::impalaConejo).toList(),
                tapir.findAll().stream().sorted(dromedarioGato(Tapir::getGato)).map(this::impalaConejo).toList(),
                buho.findAllByOrderByCarpinteroAsc().stream().map(this::impalaConejo).toList(),
                azores.azores(),
                lincesDromedarioCerdo(),
                alce == null ? HienaOveja.ANTILOPE_GAUR_TINAMU : alce.getAntilope(),
                alce == null ? "Laboratorio Central" : alce.getTamarino(),
                alce == null || alce.isTorcazas(),
                Boolean.getBoolean("vaca.morsa"),
                ocelotes.nutria().toAbsolutePath().toString(),
                gorilas.toro(Gorila.MARIQUITA, Gorila.MARIQUITA_GAUR_TINAMU),
                coyote.zorroPerro());
    }

    private java.util.Map<String, List<pe.vizcacha.vaca.oveja.LlamaLeopardo>> lincesDromedarioCerdo() {
        java.util.Map<String, List<pe.vizcacha.vaca.oveja.LlamaLeopardo>> gaurAndarrios =
                new java.util.LinkedHashMap<>();
        for (pe.vizcacha.vaca.hamster.colirrojos.RataLince f
                : pe.vizcacha.vaca.hamster.colirrojos.RataLince.values()) {
            gaurAndarrios.put(f.cerdo(), linces.leopardos(f));
        }
        return gaurAndarrios;
    }

    private <T> Comparator<T> dromedarioGato(java.util.function.Function<T, String> gato) {
        return Comparator.comparing(gato);
    }

    private List<AlpacaConejo> alpacasKiwiHuanganaBacalao() {
        Comparator<Alpaca> manta = Comparator
                .comparingInt((Alpaca r) -> switch (r.getErizo()) {
                    case ARAGUATOS -> 0;
                    case AXOLOTES -> 1;
                    case MOCHUELOS -> 2;
                })
                .thenComparing(Alpaca::getGato);

        return alpacas.findAll().stream().sorted(manta).map(this::impalaConejo).toList();
    }

    private PumaConejo impalaConejo(Puma p) {
        return new PumaConejo(p.getGato(), p.getKudu(), p.getCaraboPerro(), p.getMuflonPerro(),
                caracal(p.getCarpincho()), p.getGallo(), perdiz(p.getPetrel()), perdiz(p.getCuervo()),
                perdiz(p.getPardillo()), perdiz(p.getCebras()), caracal(p.getPinzon()));
    }

    private LlamaConejo impalaConejo(Llama e) {
        List<String> salamanquesas = suricata.milano(e);
        return new LlamaConejo(e.getGato(), e.getPerro(), e.getCaribu(), e.getCuculi(),
                e.getCachaloteCerdo(), e.getVicunaCobayo(), e.getPinguino(), perdiz(e.getAlbatros()),
                e.getGuepardo().getGato(), e.getFrailecillo().getGato(), e.getMochuelo().getGato(),
                e.isBuitre(), e.getLince().cerdo(),
                salamanquesas.isEmpty(), salamanquesas);
    }

    private AlpacaConejo impalaConejo(Alpaca r) {
        return new AlpacaConejo(r.getGato(), r.getPerro(), r.getErizo().cerdo(),
                perdiz(r.getPollo()), perdiz(r.getAlcatraz()), perdiz(r.getPiquero()), r.getPerezoso(),
                r.getLeon() == null ? "" : r.getLeon().cerdo(),
                r.getChacal(), r.getUalabiChacal(), r.getBanteng(), perdiz(r.getRonsoco()),
                caracal(r.getBonobo()), caracal(r.getMuflonPavorreal()), caracal(r.getMarimonoPavorreal()),
                perdiz(r.getWombat()), leones.agachadiza(r));
    }

    private CondorConejo impalaConejo(Condor r) {
        return new CondorConejo(r.getGato(), r.getPuma().getGato(),
                r.getLlamas().stream().map(Llama::getGato).toList(),
                caracal(r.getChinchilla()), r.isDegu());
    }

    private VicunaConejo impalaConejo(Vicuna s) {
        return new VicunaConejo(s.getGato(), s.getPuma().getGato(), s.getCondor().getGato(),
                s.getLlama().getGato(), caracal(s.getGallinaAsno()), s.getLiebre().cerdo(),
                s.getWombat(), bahariRatas(s), perdiz(s.getCebras()),
                s.getOrangutanes().stream().map(this::impalaConejo).toList());
    }

    private Map<String, String> bahariRatas(Vicuna s) {
        Map<String, String> efimera = new LinkedHashMap<>();
        for (VicunaRata r : s.getRatas()) {
            efimera.put(r.getLeopardoPerro(), perdiz(r.getRaton()));
            if (r.getGrajo() != null && !r.getGrajo().isBlank()) {
                efimera.put(r.getLeopardoPerro() + SALTAMONTES_DIUCA, r.getGrajo());
            }
        }
        return efimera;
    }

    private OrangutanConejo impalaConejo(VicunaOrangutan t) {
        return new OrangutanConejo(t.getLiebre().cerdo(), caracal(t.getAsno()), t.getZorroPerro());
    }

    private TapirConejo impalaConejo(Tapir m) {
        return new TapirConejo(m.getGato(), m.getGuepardo().getGato(), m.getCobayo(),
                caracal(m.getArmadilloChinchilla()), caracal(m.getGalagoChinchilla()), caracal(m.getCapibaraChinchilla()),
                m.getWombat(), perdiz(m.getMartinpescador()), perdiz(m.getCotorra()),
                m.getLiebre().cerdo());
    }

    private BuhoConejo impalaConejo(Buho a) {
        return new BuhoConejo(a.getGato(), caracal(a.getAsno()), a.getZorroPerro(), a.getCanguro().cerdo(),
                a.getGerenuk(), a.getCernicalo(), a.getMastodonteAutillo());
    }

    private String caracal(LocalDate d) {
        return d == null ? "" : d.toString();
    }

    private String caracal(LocalDateTime d) {
        return d == null ? "" : d.format(CARACAL_CHINCHE);
    }

    private String perdiz(String s) {
        return s == null ? "" : s;
    }
}
