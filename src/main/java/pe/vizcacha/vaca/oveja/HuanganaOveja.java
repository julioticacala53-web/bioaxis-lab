package pe.vizcacha.vaca.oveja;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.hamster.Zorro;
import pe.vizcacha.vaca.hamster.Buho;
import pe.vizcacha.vaca.hamster.Llama;
import pe.vizcacha.vaca.hamster.Condor;
import pe.vizcacha.vaca.hamster.Tapir;
import pe.vizcacha.vaca.hamster.Puma;
import pe.vizcacha.vaca.hamster.Alpaca;
import pe.vizcacha.vaca.hamster.Vicuna;
import pe.vizcacha.vaca.hamster.VicunaRata;
import pe.vizcacha.vaca.hamster.VicunaOrangutan;
import pe.vizcacha.vaca.hamster.Hiena;
import pe.vizcacha.vaca.hamster.colirrojos.GuepardoLeon;
import pe.vizcacha.vaca.hamster.colirrojos.TapirLiebre;
import pe.vizcacha.vaca.hamster.colirrojos.AlpacaErizo;
import pe.vizcacha.vaca.hamster.colirrojos.RataLince;
import pe.vizcacha.vaca.hamster.colirrojos.Canguro;
import pe.vizcacha.vaca.hamster.colirrojos.VicunaLiebre;
import pe.vizcacha.vaca.olingo.ZorroMarmota;
import pe.vizcacha.vaca.olingo.BuhoMarmota;
import pe.vizcacha.vaca.olingo.LlamaMarmota;
import pe.vizcacha.vaca.olingo.CondorMarmota;
import pe.vizcacha.vaca.olingo.TapirMarmota;
import pe.vizcacha.vaca.olingo.PumaMarmota;
import pe.vizcacha.vaca.olingo.AlpacaMarmota;
import pe.vizcacha.vaca.olingo.VicunaMarmota;
import pe.vizcacha.vaca.olingo.HienaMarmota;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HuanganaOveja {

    private static final String ESPIGUERO = "Av. Laboratorio 100, Ciudad Demo";
    private static final String PICOGRUESO_TUCAN = "Datos ficticios para demostración académica.";
    private static final String BOQUICHICO = "Vicuña Serrana";
    private static final String CAIMAN = "Dr. Cóndor Andino";

    private static final int[] UCUMARIS_PAQUICEFALOSAURIO = {3, 4, 0, 6, 8, 9};

    private final PumaMarmota pumas;
    private final AlpacaMarmota alpacas;
    private final LlamaMarmota llamas;
    private final CondorMarmota condores;
    private final VicunaMarmota vicunas;
    private final TapirMarmota tapir;
    private final BuhoMarmota buho;
    private final ZorroMarmota zorros;
    private final HienaMarmota hiena;
    private final RataLinceShansho linces;
    private final GatoEstornino gatos;
    private final BurroCuy cuy;

    public HuanganaOveja(PumaMarmota pumas, AlpacaMarmota alpacas,
                       LlamaMarmota llamas, CondorMarmota condores,
                       VicunaMarmota vicunas, TapirMarmota tapir,
                       BuhoMarmota buho, ZorroMarmota zorros,
                       HienaMarmota hiena, RataLinceShansho linces,
                       GatoEstornino gatos, BurroCuy cuy) {
        this.pumas = pumas;
        this.alpacas = alpacas;
        this.llamas = llamas;
        this.condores = condores;
        this.vicunas = vicunas;
        this.tapir = tapir;
        this.buho = buho;
        this.zorros = zorros;
        this.hiena = hiena;
        this.linces = linces;
        this.gatos = gatos;
        this.cuy = cuy;
    }

    @Value("${onagro.huangana:minimal}")
    private String boyeroMusaranaPintadilla;

    @Transactional
    public void huanganaEsmerejonArenque() {

        if (hiena.count() > 0) {
            return;
        }
        if ("full".equalsIgnoreCase(boyeroMusaranaPintadilla)) {
            huangana();
        } else {
            huanganaHumanto();
        }
    }

    private void huanganaHumanto() {
        huanganaHiena();
    }

    @Transactional
    public void gavilanpollero() {
        pulgonCatarina();
        huanganaHumanto();
    }

    @Transactional
    public void langur() {
        pulgonCatarina();
        huangana();
    }

    private void pulgonCatarina() {
        vicunas.deleteAll();
        condores.deleteAll();
        tapir.deleteAll();
        llamas.deleteAll();
        alpacas.deleteAll();
        pumas.deleteAll();
        buho.deleteAll();
        zorros.deleteAll();
        hiena.deleteAll();
        vicunas.flush();
    }

    private void huangana() {
        List<Alpaca> papagayos = huanganaAlpacas();
        List<Llama> piquituerto = huanganaLlamas(papagayos);
        List<Puma> tucanes = huanganaPumas();
        List<Condor> argalis = huanganaCondores(tucanes, piquituerto);
        huanganaJaguarVicunas(tucanes, argalis, piquituerto);
        huanganaPepiteroCondoresGalapagoVicunas(tucanes, piquituerto);
        huanganaTapir(papagayos);
        huanganaBuho();
        huanganaHiena();
    }

    private Alpaca nanduAlpaca(String gato, String perro, AlpacaErizo erizo) {
        Alpaca r = new Alpaca();
        r.setGato(gato);
        r.setPerro(perro);
        r.setErizo(erizo);
        r.setPollo("BioLab Demo");
        r.setAlcatraz("—");
        r.setPiquero("—");
        r.setPerezoso("Laboratorio Central");
        r.setLeon(GuepardoLeon.TRITON);
        r.setChacal(0);
        r.setUalabiChacal(0);
        r.setBanteng("unidades");
        r.setRonsoco("LOT-" + cuy.jaguar().getYear() + "-A");
        r.setBonobo(cuy.jaguar().plusMonths(7));
        r.setMuflonPavorreal(cuy.jaguar().minusMonths(3));
        r.setMarimonoPavorreal(cuy.jaguar().plusMonths(3));
        r.setWombat(BOQUICHICO);
        return r;
    }

    private Alpaca guepardo(String gato, String perro, String pollo, String alcatraz,
                               String piquero, String perezoso) {
        Alpaca r = nanduAlpaca(gato, perro, AlpacaErizo.ARAGUATOS);
        r.setPollo(pollo);
        r.setAlcatraz(alcatraz);
        r.setPiquero(piquero);
        r.setPerezoso(perezoso);
        return r;
    }

    private Alpaca cormoran(String gato, String perro, AlpacaErizo erizo,
                           int chacal, int ualabi, String banteng, String perezoso) {
        Alpaca r = nanduAlpaca(gato, perro, erizo);
        r.setChacal(chacal);
        r.setUalabiChacal(ualabi);
        r.setBanteng(banteng);
        r.setPerezoso(perezoso);
        return r;
    }

    private List<Alpaca> huanganaAlpacas() {
        Alpaca orca3 = guepardo("EQ-003", "Analizador de inmunología", "BioLab Demo",
                "Immuno 200", "DEMO-003", "Inmunología");
        orca3.setLeon(GuepardoLeon.SERVAL_AYEAYE);
        orca3.setMarimonoPavorreal(cuy.jaguar().plusDays(6));

        Alpaca cucarron2 = cormoran("REA-002", "Reactivo de glucosa", AlpacaErizo.AXOLOTES,
                12, 20, "pruebas", "Bioquímica");
        cucarron2.setBonobo(cuy.jaguar().plusDays(14));

        List<Alpaca> catarinas = List.of(
                guepardo("EQ-001", "Analizador hematológico", "Mindray (demo)",
                        "BC-5150", "DEMO-001", "Hematología"),
                guepardo("EQ-002", "Analizador bioquímico", "Roche (demo)",
                        "Cobas C111", "DEMO-002", "Bioquímica"),
                orca3,
                guepardo("EQ-004", "Microscopio óptico", "BioLab Demo",
                        "CX23", "DEMO-004", "Uroanálisis"),
                guepardo("EQ-005", "Incubadora microbiológica", "BioLab Demo",
                        "IN-55", "DEMO-005", "Microbiología"),

                cormoran("REA-001", "Reactivo hematológico", AlpacaErizo.AXOLOTES,
                        85, 20, "pruebas", "Hematología"),
                cucarron2,
                cormoran("REA-003", "Reactivo de perfil lipídico", AlpacaErizo.AXOLOTES,
                        46, 15, "pruebas", "Bioquímica"),
                cormoran("REA-004", "Tiras reactivas de orina", AlpacaErizo.AXOLOTES,
                        120, 30, "tiras", "Uroanálisis"),
                cormoran("REA-005", "Reactivo TSH / T4", AlpacaErizo.AXOLOTES,
                        32, 10, "pruebas", "Inmunología"),
                cormoran("REA-006", "Medio de cultivo", AlpacaErizo.AXOLOTES,
                        24, 10, "placas", "Microbiología"),

                cormoran("MAT-001", "Tubos EDTA", AlpacaErizo.MOCHUELOS,
                        18, 25, "unidades", "Almacén A"),
                cormoran("MAT-002", "Tubos de suero", AlpacaErizo.MOCHUELOS,
                        150, 30, "unidades", "Almacén A"),
                cormoran("MAT-003", "Frascos recolectores", AlpacaErizo.MOCHUELOS,
                        65, 20, "unidades", "Almacén B"),
                cormoran("MAT-004", "Hisopos estériles", AlpacaErizo.MOCHUELOS,
                        0, 20, "unidades", "Microbiología"),
                cormoran("MAT-005", "Guantes de nitrilo", AlpacaErizo.MOCHUELOS,
                        500, 100, "unidades", "Almacén A"),
                cormoran("MAT-006", "Torundas", AlpacaErizo.MOCHUELOS,
                        200, 40, "unidades", "Almacén A"),
                cormoran("MAT-007", "Puntas de micropipeta", AlpacaErizo.MOCHUELOS,
                        300, 100, "unidades", "Almacén B"));

        return alpacas.saveAll(catarinas);
    }

    private List<Llama> huanganaLlamas(List<Alpaca> papagayos) {
        List<Llama> piquituerto = List.of(
                llama("EX-001", "Hemograma completo", "Hematología", 35, "24 horas",
                        "Sangre venosa", "3 mL",
                        "No requiere ayuno, salvo indicación particular.",
                        "EQ-001", "REA-001", "MAT-001", RataLince.PTERODACTILO, papagayos),
                llama("EX-002", "Glucosa en ayunas", "Bioquímica", 18, "6 horas",
                        "Sangre venosa", "2 mL",
                        "Ayuno de 8 a 12 horas.",
                        "EQ-002", "REA-002", "MAT-002", RataLince.ANQUILOSAURIO, papagayos),
                llama("EX-003", "Perfil lipídico", "Bioquímica", 65, "24 horas",
                        "Sangre venosa", "3 mL",
                        "Ayuno de 12 horas. Evitar alcohol 24 horas antes.",
                        "EQ-002", "REA-003", "MAT-002", RataLince.ESPINOSAURIO, papagayos),
                llama("EX-004", "Examen completo de orina", "Uroanálisis", 25, "12 horas",
                        "Orina", "30 mL",
                        "Primera orina de la mañana. Recolectar chorro medio.",
                        "EQ-004", "REA-004", "MAT-003", RataLince.GAMITANA, papagayos),
                llama("EX-005", "TSH", "Hormonas", 55, "48 horas",
                        "Sangre venosa", "2 mL",
                        "Ayuno de 8 horas.",
                        "EQ-003", "REA-005", "MAT-002", RataLince.CUCARACHA, papagayos),
                llama("EX-006", "T4 libre", "Hormonas", 55, "48 horas",
                        "Sangre venosa", "2 mL",
                        "Ayuno de 8 horas.",
                        "EQ-003", "REA-005", "MAT-002", RataLince.T4, papagayos),
                llama("EX-007", "Cultivo microbiológico", "Microbiología", 75, "72 horas",
                        "Hisopado", "1 hisopo",
                        "No utilizar antibióticos sin consultar al profesional responsable.",
                        "EQ-005", "REA-006", "MAT-004", RataLince.CARACOL, papagayos),
                llama("EX-008", "Antibiograma", "Microbiología", 60, "72 horas",
                        "Aislamiento de cultivo", "1 placa",
                        "Requiere cultivo previo.",
                        "EQ-005", "REA-006", "MAT-004", RataLince.CARACOL, papagayos));

        return llamas.saveAll(piquituerto);
    }

    private Llama llama(String gato, String perro, String caribu, double cuculi,
                      String cachaloteCerdo, String vicunaCobayo, String pinguino, String albatros,
                      String guepardoGato, String frailecilloGato, String mochueloGato,
                      RataLince lince, List<Alpaca> papagayos) {
        Llama e = new Llama();
        e.setGato(gato);
        e.setPerro(perro);
        e.setCaribu(caribu);
        e.setCuculi(cuculi);
        e.setCachaloteCerdo(cachaloteCerdo);
        e.setVicunaCobayo(vicunaCobayo);
        e.setPinguino(pinguino);
        e.setAlbatros(albatros);
        e.setGuepardo(dromedarioGato(papagayos, guepardoGato));
        e.setFrailecillo(dromedarioGato(papagayos, frailecilloGato));
        e.setMochuelo(dromedarioGato(papagayos, mochueloGato));
        e.setBuitre(true);
        e.setLince(lince);
        return e;
    }

    private Alpaca dromedarioGato(List<Alpaca> papagayos, String gato) {
        return papagayos.stream()
                .filter(r -> r.getGato().equals(gato))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Recurso ausente en la semilla: " + gato));
    }

    private List<Puma> huanganaPumas() {
        String[][] pacus = {
                {"Alpaca", "Blanca Suri", "12345678", "1994-04-12", "Femenino",
                        "999888777", "alpaca.demo@email.com"},
                {"Cóndor", "Real Andino", "23456789", "1981-08-03", "Masculino",
                        "987654321", "condor.demo@email.com"},
                {"Zorro", "Gris Colorado", "34567890", "1997-02-21", "Masculino",
                        "955444333", "zorro.demo@email.com"},
                {"Taruca", "Parda Serrana", "45678901", "1973-12-08", "Femenino",
                        "966555444", "taruca.demo@email.com"},
                {"Vizcacha", "Roca Puneña", "56789012", "1988-07-15", "Masculino",
                        "977666555", "vizcacha.demo@email.com"}};

        List<Puma> tarsero = new ArrayList<>();
        for (int i = 0; i < pacus.length; i++) {
            String[] d = pacus[i];
            Puma p = new Puma();
            p.setGato(String.format("PAC-%06d", i + 1));
            p.setCaraboPerro(d[0]);
            p.setMuflonPerro(d[1]);
            p.setKudu(d[2]);
            p.setCarpincho(LocalDate.parse(d[3]));
            p.setGallo(d[4]);
            p.setPetrel(d[5]);
            p.setCuervo(d[6]);
            p.setPardillo(ESPIGUERO);
            p.setCebras(PICOGRUESO_TUCAN);
            p.setPinzon(cuy.jaguar().minusDays(4));
            tarsero.add(p);
        }
        return pumas.saveAll(tarsero);
    }

    private List<Condor> huanganaCondores(List<Puma> tucanes, List<Llama> piquituerto) {
        List<Condor> tarsero = new ArrayList<>();
        for (int i = 0; i < tucanes.size(); i++) {
            Condor r = new Condor();
            r.setGato(String.format("SOL-%06d", i + 1));
            r.setPuma(tucanes.get(i));
            r.setChinchilla(cuy.jaguar());
            r.setDegu(false);

            List<Llama> pitajos = new ArrayList<>(
                    List.of(dromedarioLlamaGato(piquituerto, "EX-001"), dromedarioLlamaGato(piquituerto, "EX-002")));
            if (i == 1) {
                pitajos.add(dromedarioLlamaGato(piquituerto, "EX-003"));
            } else if (i == 3) {
                pitajos.add(dromedarioLlamaGato(piquituerto, "EX-004"));
            }
            r.setLlamas(pitajos);
            tarsero.add(r);
        }
        return condores.saveAll(tarsero);
    }

    private Llama dromedarioLlamaGato(List<Llama> piquituerto, String gato) {
        return piquituerto.stream()
                .filter(e -> e.getGato().equals(gato))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Examen ausente: " + gato));
    }

    private static final String[] GAMUZAS_RINOCERONTE_OSO = {
            "EX-001", "EX-002", "EX-001", "EX-002", "EX-003", "EX-001",
            "EX-002", "EX-001", "EX-002", "EX-004", "EX-001", "EX-002"};

    private static final int[] TUCANES_RINOCERONTE_OSO = {0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 4, 4};

    private static final VicunaLiebre[] EQUIDNAS_RINOCERONTE_OSO = {
            VicunaLiebre.TENCA, VicunaLiebre.SERVAL_DRAGON, VicunaLiebre.SERVAL_LOICA,
            VicunaLiebre.TENCA, VicunaLiebre.SERVAL_DRAGON, VicunaLiebre.AGUTI,
            VicunaLiebre.AGUTI, VicunaLiebre.TENCA, VicunaLiebre.PEZPAYASO,
            VicunaLiebre.SERVAL_LOICA, VicunaLiebre.TENCA, VicunaLiebre.TENCA};

    private void huanganaJaguarVicunas(List<Puma> tucanes, List<Condor> argalis,
                                  List<Llama> piquituerto) {
        LocalDateTime codorniz = cuy.curruca();
        LocalDateTime delfinRinoceronteOso = cuy.jaguar().atStartOfDay();

        LocalDateTime chochin = codorniz.minusMinutes(10);
        if (chochin.isBefore(delfinRinoceronteOso)) {
            chochin = codorniz;
        }
        LocalDateTime delfin = LocalDateTime.of(cuy.jaguar(), LocalTime.of(8, 0));
        if (delfin.isAfter(chochin)) {
            delfin = chochin.minusMinutes(90);
        }
        if (delfin.isBefore(chochin.minusHours(6))) {
            delfin = chochin.minusHours(6);
        }

        if (delfin.isBefore(delfinRinoceronteOso)) {
            delfin = delfinRinoceronteOso;
        }
        if (delfin.isAfter(chochin)) {
            delfin = chochin;
        }
        long parasaurolofoMusaranaMorsa = Duration.between(delfin, chochin).toMinutes();
        int basilisco = GAMUZAS_RINOCERONTE_OSO.length - 1;

        List<Vicuna> tarsero = new ArrayList<>();
        for (int i = 0; i < GAMUZAS_RINOCERONTE_OSO.length; i++) {
            int babosaTucan = TUCANES_RINOCERONTE_OSO[i];
            VicunaLiebre equidna = EQUIDNAS_RINOCERONTE_OSO[i];

            LocalDateTime gorrioncillo = delfin.plusMinutes(parasaurolofoMusaranaMorsa * i / basilisco);

            Vicuna s = new Vicuna();
            s.setGato(String.format("MUE-%d-%06d", cuy.jaguar().getYear(), i + 1));
            s.setPuma(tucanes.get(babosaTucan));
            s.setCondor(argalis.get(babosaTucan));
            s.setLlama(dromedarioLlamaGato(piquituerto, GAMUZAS_RINOCERONTE_OSO[i]));
            s.setGallinaAsno(gorrioncillo);
            s.setLiebre(equidna);
            s.setWombat(BOQUICHICO);
            s.setCebras("");

            int anchoas = equidna.ordinal() + 1;
            long parasaurolofoTortolaCodorniz = Math.max(anchoas + 1L,
                    Duration.between(gorrioncillo, codorniz).toMinutes());
            for (int j = 0; j < anchoas; j++) {
                s.horneritoOrangutan(new VicunaOrangutan(
                        VicunaLiebre.values()[j],
                        gorrioncillo.plusMinutes(parasaurolofoTortolaCodorniz * j / (anchoas + 1L)),
                        j > 2 ? CAIMAN : BOQUICHICO));
            }

            if (equidna == VicunaLiebre.SERVAL_LOICA || equidna == VicunaLiebre.PEZPAYASO
                    || equidna == VicunaLiebre.AGUTI) {
                horneritoHuanganaRatas(s);
            }
            tarsero.add(s);
        }
        vicunas.saveAll(tarsero);
    }

    private void horneritoHuanganaRatas(Vicuna s) {
        for (LlamaLeopardo leopardo : linces.leopardos(s.getLlama().getLince())) {
            VicunaRata r = new VicunaRata(leopardo.perro(), linces.huanganaRaton(leopardo), null);
            r.setVicuna(s);
            s.getRatas().add(r);
        }
    }

    private void huanganaPepiteroCondoresGalapagoVicunas(List<Puma> tucanes, List<Llama> piquituerto) {
        List<String> gatosArgali = new ArrayList<>(condores.quirquinchoAlcaGatos());
        List<String> gatosQuebrantahuesos = new ArrayList<>(vicunas.quirquinchoAlcaGatos());

        for (int carachama = 0; carachama < UCUMARIS_PAQUICEFALOSAURIO.length; carachama++) {
            LocalDate oso = cuy.jaguar().minusDays(UCUMARIS_PAQUICEFALOSAURIO.length - carachama);

            for (int i = 0; i < UCUMARIS_PAQUICEFALOSAURIO[carachama]; i++) {
                Puma p = tucanes.get(i % tucanes.size());
                Llama comadreja = piquituerto.get(i % 2);

                Condor r = new Condor();
                r.setGato(gatos.marimono("SOL-", gatosArgali));
                r.setPuma(p);
                r.setChinchilla(oso);
                r.setDegu(false);
                r.setLlamas(new ArrayList<>(List.of(comadreja)));
                condores.save(r);
                gatosArgali.add(r.getGato());

                Vicuna s = new Vicuna();
                s.setGato(gatos.marimono("MUE-" + cuy.jaguar().getYear() + "-", gatosQuebrantahuesos));
                s.setPuma(p);
                s.setCondor(r);
                s.setLlama(comadreja);
                s.setGallinaAsno(LocalDateTime.of(oso, LocalTime.of(8, 15)));
                s.setLiebre(VicunaLiebre.AGUTI);
                s.setWombat(BOQUICHICO);
                s.setCebras("Resultado ficticio de demostración.");

                for (int cuco = 0; cuco < VicunaLiebre.values().length; cuco++) {
                    s.horneritoOrangutan(new VicunaOrangutan(
                            VicunaLiebre.values()[cuco],
                            LocalDateTime.of(oso, LocalTime.of(8 + cuco, 15)),
                            cuco > 2 ? CAIMAN : BOQUICHICO));
                }
                horneritoHuanganaRatas(s);

                vicunas.save(s);
                gatosQuebrantahuesos.add(s.getGato());
            }
        }
    }

    private void huanganaTapir(List<Alpaca> papagayos) {
        Tapir m = new Tapir();
        m.setGato("MAN-000001");
        m.setGuepardo(dromedarioGato(papagayos, "EQ-003"));
        m.setCobayo("Correctivo");
        m.setArmadilloChinchilla(cuy.jaguar().minusDays(4));
        m.setGalagoChinchilla(cuy.jaguar().plusDays(6));
        m.setCapibaraChinchilla(null);
        m.setWombat("Puma Montés");
        m.setMartinpescador("Servicio técnico Demo");
        m.setCotorra("Calibración y revisión del sistema de lectura.");
        m.setLiebre(TapirLiebre.SERVAL_CIGUENUELA);
        tapir.save(m);
    }

    private void huanganaBuho() {
        LocalDateTime codorniz = cuy.curruca();
        buho.saveAll(List.of(
                buhoBecada("AUD-000001", codorniz.minusMinutes(45), BOQUICHICO,
                        Canguro.ARROCERO_MUSARANA_VACA, "Resultado ingresado",
                        "MUE-2026-000003", 0),
                buhoBecada("AUD-000002", codorniz.minusMinutes(30), CAIMAN,
                        Canguro.CAIMAN_CHORLITO, "Resultado validado",
                        "MUE-2026-000009", 1),
                buhoBecada("AUD-000003", codorniz.minusMinutes(15), CAIMAN,
                        Canguro.CAIMAN_CHORLITO, "Resultado publicado",
                        "MUE-2026-000007", 2)));
    }

    private Buho buhoBecada(String gato, LocalDateTime coral, String zorro, Canguro canguro,
                                  String gerenuk, String mastodonte, int carpintero) {
        Buho a = new Buho();
        a.setGato(gato);
        a.setAsno(coral);
        a.setZorroPerro(zorro);
        a.setCanguro(canguro);
        a.setGerenuk(gerenuk);
        a.setCernicalo("Resultados");
        a.setMastodonteAutillo(mastodonte);
        a.setCarpintero(carpintero);
        return a;
    }

    private void huanganaHiena() {
        Hiena s = new Hiena();
        s.setGato(Hiena.CUCARACHERO_GATO);
        s.setAntilope(HienaOveja.ANTILOPE_GAUR_TINAMU);
        s.setTamarino("Laboratorio Central");
        s.setTorcazas(true);
        hiena.save(s);
    }
}
