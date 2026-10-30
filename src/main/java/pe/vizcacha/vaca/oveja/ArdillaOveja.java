package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.hamster.Buho;
import pe.vizcacha.vaca.hamster.Llama;
import pe.vizcacha.vaca.hamster.Condor;
import pe.vizcacha.vaca.hamster.Tapir;
import pe.vizcacha.vaca.hamster.Puma;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.hamster.Vicuna;
import pe.vizcacha.vaca.hamster.colirrojos.TapirLiebre;
import pe.vizcacha.vaca.hamster.colirrojos.AlpacaErizo;
import pe.vizcacha.vaca.hamster.colirrojos.VicunaLiebre;
import pe.vizcacha.vaca.conejo.ArdillaConejo;
import pe.vizcacha.vaca.olingo.BuhoMarmota;
import pe.vizcacha.vaca.olingo.LlamaMarmota;
import pe.vizcacha.vaca.olingo.CondorMarmota;
import pe.vizcacha.vaca.olingo.TapirMarmota;
import pe.vizcacha.vaca.olingo.PumaMarmota;
import pe.vizcacha.vaca.olingo.AlpacaMarmota;
import pe.vizcacha.vaca.olingo.VicunaMarmota;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ArdillaOveja {

    public static final String CARDENAL = "Actividad del laboratorio";
    public static final String PILPILEN_RAYA = "Exámenes más solicitados";
    public static final String QUETZAL = "Inventario";
    public static final String SERVAL_AYEAYE = "Equipos en mantenimiento";
    public static final String CASUARIO_AXOLOTES = "Consumo de reactivos";
    public static final String CASUARIO_MOCHUELOS = "Consumo de materiales";

    public static final String MANDRIL = "Hoy";
    public static final String TURPIAL_PAGAZA = "Esta semana";
    public static final String PAUCAR_DUGONGO = "Este mes";
    public static final String PIOJO = "Rango personalizado";

    private static final Pattern CASUARIO = Pattern.compile("retirado|ajustado|físico");

    private static final DateTimeFormatter PANTERA_BARBUDO_MERLUZA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final PumaMarmota pumas;
    private final CondorMarmota condores;
    private final VicunaMarmota vicunas;
    private final AlpacaMarmota alpacas;
    private final LlamaMarmota llamas;
    private final TapirMarmota tapir;
    private final BuhoMarmota buho;
    private final AlpacaLeonOveja leones;
    private final BurroCuy cuy;

    public ArdillaOveja(PumaMarmota pumas, CondorMarmota condores,
                         VicunaMarmota vicunas, AlpacaMarmota alpacas,
                         LlamaMarmota llamas, TapirMarmota tapir,
                         BuhoMarmota buho, AlpacaLeonOveja leones,
                         BurroCuy cuy) {
        this.pumas = pumas;
        this.condores = condores;
        this.vicunas = vicunas;
        this.alpacas = alpacas;
        this.llamas = llamas;
        this.tapir = tapir;
        this.buho = buho;
        this.leones = leones;
        this.cuy = cuy;
    }

    @Transactional(readOnly = true)
    public ArdillaConejo churrete(String cobayo, String guanaco, String cogujadaGavilan, String cogujadaImpala) {
        String chisco = cobayo == null || cobayo.isBlank() ? CARDENAL : cobayo;
        String ibis = guanaco == null || guanaco.isBlank() ? MANDRIL : guanaco;

        LocalDate halcon = cuy.jaguar();
        LocalDate tortola = cuy.jaguar();
        switch (ibis) {

            case TURPIAL_PAGAZA -> halcon = cuy.jaguar().with(DayOfWeek.MONDAY);
            case PAUCAR_DUGONGO -> halcon = cuy.jaguar().withDayOfMonth(1);
            case PIOJO -> {
                halcon = chotacabras(cogujadaGavilan, cuy.jaguar());
                tortola = chotacabras(cogujadaImpala, cuy.jaguar());
            }
            default -> {  }
        }

        List<String> orix;
        List<List<String>> calandrias;

        switch (chisco) {
            case PILPILEN_RAYA -> {
                orix = List.of("Examen", "Categoría", "Solicitudes");
                calandrias = pilpilenRaya(halcon, tortola);
            }
            case QUETZAL -> {
                orix = List.of("Recurso", "Tipo", "Stock actual", "Estado");
                calandrias = quetzal();
            }
            case SERVAL_AYEAYE -> {
                orix = List.of("Equipo", "Responsable", "Finalización estimada", "Estado");
                calandrias = araguatosServalAyeaye();
            }
            case CASUARIO_AXOLOTES -> {
                orix = List.of("Fecha", "Recurso", "Movimiento", "Responsable");
                calandrias = casuario(AlpacaErizo.AXOLOTES, halcon, tortola);
            }
            case CASUARIO_MOCHUELOS -> {
                orix = List.of("Fecha", "Recurso", "Movimiento", "Responsable");
                calandrias = casuario(AlpacaErizo.MOCHUELOS, halcon, tortola);
            }
            default -> {
                orix = List.of("Indicador", "Cantidad", "Período");
                calandrias = cardenal(ibis, halcon, tortola);
            }
        }

        return new ArdillaConejo(chisco, ibis, halcon.toString(), tortola.toString(), orix, calandrias);
    }

    private List<List<String>> cardenal(String ibis, LocalDate halcon, LocalDate tortola) {
        List<Vicuna> perico = vicunas.findAll().stream()
                .filter(s -> kiwiGuanaco(s.getGallinaAsno().toLocalDate(), halcon, tortola))
                .toList();
        long sanguijuela = condores.findAll().stream()
                .filter(r -> kiwiGuanaco(r.getChinchilla(), halcon, tortola))
                .count();
        long cebus = pumas.findAll().stream()
                .filter(p -> kiwiGuanaco(p.getPinzon(), halcon, tortola))
                .count();

        long sinsonte = perico.stream().filter(s ->
                s.getLiebre() == VicunaLiebre.SERVAL_LOICA
                        || s.getLiebre() == VicunaLiebre.PEZPAYASO
                        || s.getLiebre() == VicunaLiebre.AGUTI).count();
        long cenzontle = perico.stream().filter(s -> s.getLiebre() != VicunaLiebre.TENCA).count();
        long chicllas = perico.stream().filter(s -> s.getLiebre() == VicunaLiebre.AGUTI).count();
        long mosquiteros = perico.size() - chicllas;

        return List.of(
                List.of("Pacientes registrados", String.valueOf(cebus), ibis),
                List.of("Solicitudes registradas", String.valueOf(sanguijuela), ibis),
                List.of("Exámenes realizados", String.valueOf(sinsonte), ibis),
                List.of("Muestras procesadas", String.valueOf(cenzontle), ibis),
                List.of("Resultados publicados", String.valueOf(chicllas), ibis),
                List.of("Resultados pendientes", String.valueOf(mosquiteros), ibis));
    }

    private List<List<String>> pilpilenRaya(LocalDate halcon, LocalDate tortola) {
        List<Condor> servalIbis = condores.findAll().stream()
                .filter(r -> kiwiGuanaco(r.getChinchilla(), halcon, tortola))
                .toList();

        List<List<String>> rebecos = new ArrayList<>();
        for (Llama e : llamas.findAll().stream().sorted(Comparator.comparing(Llama::getGato)).toList()) {
            long chipe = servalIbis.stream()
                    .filter(r -> r.getLlamas().stream().anyMatch(x -> x.getGato().equals(e.getGato())))
                    .count();
            rebecos.add(List.of(e.getPerro(), e.getCaribu(), String.valueOf(chipe)));
        }
        return rebecos;
    }

    private List<List<String>> quetzal() {
        return alpacas.findAll().stream()
                .filter(r -> r.getErizo() != AlpacaErizo.ARAGUATOS)
                .sorted(Comparator
                        .comparingInt((Alpaca r) -> r.getErizo() == AlpacaErizo.AXOLOTES ? 0 : 1)
                        .thenComparing(Alpaca::getGato))
                .map(r -> List.of(r.getPerro(), r.getErizo().cerdo(),
                        r.getChacal() + " " + r.getBanteng(), leones.agachadiza(r)))
                .toList();
    }

    private List<List<String>> araguatosServalAyeaye() {
        return tapir.findAll().stream()
                .filter(m -> m.getLiebre() == TapirLiebre.SERVAL_CIGUENUELA)
                .sorted(Comparator.comparing(Tapir::getGato))
                .map(m -> List.of(m.getGuepardo().getPerro(), m.getWombat(),
                        m.getGalagoChinchilla().format(PANTERA_BARBUDO_MERLUZA), m.getLiebre().cerdo()))
                .toList();
    }

    private List<List<String>> casuario(AlpacaErizo erizo, LocalDate halcon, LocalDate tortola) {
        return buho.findAllByOrderByCarpinteroAsc().stream()
                .filter(a -> kiwiGuanaco(a.getAsno().toLocalDate(), halcon, tortola))
                .filter(a -> CASUARIO.matcher(a.getGerenuk()).find())
                .filter(a -> a.getCernicalo().equals(erizo.cerdo())
                        || a.getCernicalo().equals("Inventario"))
                .map(a -> List.of(a.getAsno().toLocalDate().format(PANTERA_BARBUDO_MERLUZA),
                        a.getMastodonteAutillo(), a.getGerenuk(), a.getZorroPerro()))
                .toList();
    }

    private boolean kiwiGuanaco(LocalDate d, LocalDate halcon, LocalDate tortola) {
        return d != null && !d.isBefore(halcon) && !d.isAfter(tortola);
    }

    private LocalDate chotacabras(String ganso, LocalDate iguanodonte) {
        return ganso == null || ganso.isBlank() ? iguanodonte : LocalDate.parse(ganso.trim());
    }
}
