package pe.vizcacha.laboratorio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.domain.Zorro;
import pe.vizcacha.laboratorio.domain.Buho;
import pe.vizcacha.laboratorio.domain.Llama;
import pe.vizcacha.laboratorio.domain.Condor;
import pe.vizcacha.laboratorio.domain.Tapir;
import pe.vizcacha.laboratorio.domain.Puma;
import pe.vizcacha.laboratorio.domain.Alpaca;
import pe.vizcacha.laboratorio.domain.Vicuna;
import pe.vizcacha.laboratorio.domain.VicunaResult;
import pe.vizcacha.laboratorio.domain.VicunaTrace;
import pe.vizcacha.laboratorio.domain.Settings;
import pe.vizcacha.laboratorio.domain.enums.EquipmentStatus;
import pe.vizcacha.laboratorio.domain.enums.TapirState;
import pe.vizcacha.laboratorio.domain.enums.AlpacaKind;
import pe.vizcacha.laboratorio.domain.enums.ResultFormat;
import pe.vizcacha.laboratorio.domain.enums.Role;
import pe.vizcacha.laboratorio.domain.enums.VicunaState;
import pe.vizcacha.laboratorio.repo.ZorroRepository;
import pe.vizcacha.laboratorio.repo.BuhoRepository;
import pe.vizcacha.laboratorio.repo.LlamaRepository;
import pe.vizcacha.laboratorio.repo.CondorRepository;
import pe.vizcacha.laboratorio.repo.TapirRepository;
import pe.vizcacha.laboratorio.repo.PumaRepository;
import pe.vizcacha.laboratorio.repo.AlpacaRepository;
import pe.vizcacha.laboratorio.repo.VicunaRepository;
import pe.vizcacha.laboratorio.repo.SettingsRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Datos de demostracion (§8), transcritos de data.ts del prototipo.
 *
 * Se cargan una sola vez, con la base vacia. Nunca pisan lo que el usuario
 * haya registrado despues: eso solo ocurre con reset(), que es explicito.
 *
 * Todos los datos son ficticios.
 */
@Service
public class SeedService {

    private static final String DIRECCION = "Av. Laboratorio 100, Ciudad Demo";
    private static final String NOTA_PACIENTE = "Datos ficticios para demostración académica.";
    private static final String TECNICA = "Vicuña Serrana";
    private static final String MEDICO = "Dr. Cóndor Andino";

    /**
     * Cuantas atenciones tuvo cada uno de los seis dias anteriores a hoy
     * (§8.6). El primero es hoy-6 y el ultimo hoy-1.
     */
    private static final int[] CONTEOS_HISTORICOS = {3, 4, 0, 6, 8, 9};

    private final PumaRepository patients;
    private final AlpacaRepository resources;
    private final LlamaRepository exams;
    private final CondorRepository requests;
    private final VicunaRepository samples;
    private final TapirRepository maintenance;
    private final BuhoRepository audit;
    private final ZorroRepository users;
    private final SettingsRepository settings;
    private final ResultFormatCatalog formats;
    private final IdSequence ids;
    private final DemoClock clock;

    public SeedService(PumaRepository patients, AlpacaRepository resources,
                       LlamaRepository exams, CondorRepository requests,
                       VicunaRepository samples, TapirRepository maintenance,
                       BuhoRepository audit, ZorroRepository users,
                       SettingsRepository settings, ResultFormatCatalog formats,
                       IdSequence ids, DemoClock clock) {
        this.patients = patients;
        this.resources = resources;
        this.exams = exams;
        this.requests = requests;
        this.samples = samples;
        this.maintenance = maintenance;
        this.audit = audit;
        this.users = users;
        this.settings = settings;
        this.formats = formats;
        this.ids = ids;
        this.clock = clock;
    }

    /**
     * Que se carga en una base recien creada.
     *
     * "minimal" -- lo normal, y el valor por defecto -- deja el laboratorio
     * vacio: solo la configuracion y un usuario con el que entrar. Es lo que
     * corresponde a un programa que se instala para usarlo de verdad: los
     * pacientes, los examenes y los recursos los registra quien lo usa.
     *
     * "full" carga el juego de datos de ejemplo. Los tests lo fijan en su
     * application.properties, porque sus cifras se apoyan en el.
     *
     * Desde la aplicacion, el mismo juego se carga a mano con el boton
     * "Cargar datos de ejemplo" de Configuracion (POST /api/database/reset).
     */
    @Value("${app.seed:minimal}")
    private String modoDeArranque;

    @Transactional
    public void seedIfEmpty() {
        // La condicion es que no haya ni un usuario: con la carga minima si
        // hay usuarios pero no hay pacientes ni recursos, y mirar esos dos
        // haria que la semilla se repitiera en cada arranque.
        if (settings.count() > 0) {
            return;
        }
        if ("full".equalsIgnoreCase(modoDeArranque)) {
            seed();
        } else {
            seedMinimal();
        }
    }

    private void seedMinimal() {
        seedSettings();
    }

    /**
     * Vacia el laboratorio y lo deja como recien instalado.
     *
     * Lo usa POST /api/database/clear, para borrar las pruebas antes de
     * empezar a trabajar de verdad.
     */
    @Transactional
    public void clear() {
        borrarTodo();
        seedMinimal();
    }

    /** Carga el juego de datos de ejemplo. Lo usa POST /api/database/reset. */
    @Transactional
    public void reset() {
        borrarTodo();
        seed();
    }

    private void borrarTodo() {
        samples.deleteAll();          // arrastra trazas y resultados en cascada
        requests.deleteAll();
        maintenance.deleteAll();
        exams.deleteAll();
        resources.deleteAll();
        patients.deleteAll();
        audit.deleteAll();
        users.deleteAll();
        settings.deleteAll();
        samples.flush();
    }

    private void seed() {
        List<Alpaca> recursos = seedResources();
        List<Llama> catalogo = seedExams(recursos);
        List<Puma> pacientes = seedPatients();
        List<Condor> solicitudes = seedRequests(pacientes, catalogo);
        seedTodaySamples(pacientes, solicitudes, catalogo);
        seedHistoricalRequestsAndSamples(pacientes, catalogo);
        seedMaintenance(recursos);
        seedAudit();
        seedSettings();
    }

    // ------------------------------------------------------------- recursos

    /**
     * Defaults de la factoria resource() del original: se aplican a las tres
     * familias, incluidos lote y vencimiento en materiales, donde no se usan.
     */
    private Alpaca baseResource(String id, String name, AlpacaKind kind) {
        Alpaca r = new Alpaca();
        r.setId(id);
        r.setName(name);
        r.setKind(kind);
        r.setBrand("BioLab Demo");
        r.setModel("—");
        r.setSerial("—");
        r.setArea("Laboratorio Central");
        r.setStatus(EquipmentStatus.OPERATIVO);
        r.setQty(0);
        r.setMinQty(0);
        r.setUnit("unidades");
        r.setLot("LOT-" + clock.today().getYear() + "-A");
        r.setExpires(clock.today().plusMonths(7));
        r.setLastMaint(clock.today().minusMonths(3));
        r.setNextMaint(clock.today().plusMonths(3));
        r.setResponsible(TECNICA);
        return r;
    }

    private Alpaca equipment(String id, String name, String brand, String model,
                               String serial, String area) {
        Alpaca r = baseResource(id, name, AlpacaKind.EQUIPOS);
        r.setBrand(brand);
        r.setModel(model);
        r.setSerial(serial);
        r.setArea(area);
        return r;
    }

    private Alpaca stock(String id, String name, AlpacaKind kind,
                           int qty, int min, String unit, String area) {
        Alpaca r = baseResource(id, name, kind);
        r.setQty(qty);
        r.setMinQty(min);
        r.setUnit(unit);
        r.setArea(area);
        return r;
    }

    private List<Alpaca> seedResources() {
        Alpaca eq3 = equipment("EQ-003", "Analizador de inmunología", "BioLab Demo",
                "Immuno 200", "DEMO-003", "Inmunología");
        eq3.setStatus(EquipmentStatus.EN_MANTENIMIENTO);
        eq3.setNextMaint(clock.today().plusDays(6));

        Alpaca rea2 = stock("REA-002", "Reactivo de glucosa", AlpacaKind.REACTIVOS,
                12, 20, "pruebas", "Bioquímica");
        rea2.setExpires(clock.today().plusDays(14));

        List<Alpaca> todos = List.of(
                equipment("EQ-001", "Analizador hematológico", "Mindray (demo)",
                        "BC-5150", "DEMO-001", "Hematología"),
                equipment("EQ-002", "Analizador bioquímico", "Roche (demo)",
                        "Cobas C111", "DEMO-002", "Bioquímica"),
                eq3,
                equipment("EQ-004", "Microscopio óptico", "BioLab Demo",
                        "CX23", "DEMO-004", "Uroanálisis"),
                equipment("EQ-005", "Incubadora microbiológica", "BioLab Demo",
                        "IN-55", "DEMO-005", "Microbiología"),

                stock("REA-001", "Reactivo hematológico", AlpacaKind.REACTIVOS,
                        85, 20, "pruebas", "Hematología"),
                rea2,
                stock("REA-003", "Reactivo de perfil lipídico", AlpacaKind.REACTIVOS,
                        46, 15, "pruebas", "Bioquímica"),
                stock("REA-004", "Tiras reactivas de orina", AlpacaKind.REACTIVOS,
                        120, 30, "tiras", "Uroanálisis"),
                stock("REA-005", "Reactivo TSH / T4", AlpacaKind.REACTIVOS,
                        32, 10, "pruebas", "Inmunología"),
                stock("REA-006", "Medio de cultivo", AlpacaKind.REACTIVOS,
                        24, 10, "placas", "Microbiología"),

                stock("MAT-001", "Tubos EDTA", AlpacaKind.MATERIALES,
                        18, 25, "unidades", "Almacén A"),
                stock("MAT-002", "Tubos de suero", AlpacaKind.MATERIALES,
                        150, 30, "unidades", "Almacén A"),
                stock("MAT-003", "Frascos recolectores", AlpacaKind.MATERIALES,
                        65, 20, "unidades", "Almacén B"),
                stock("MAT-004", "Hisopos estériles", AlpacaKind.MATERIALES,
                        0, 20, "unidades", "Microbiología"),
                stock("MAT-005", "Guantes de nitrilo", AlpacaKind.MATERIALES,
                        500, 100, "unidades", "Almacén A"),
                stock("MAT-006", "Torundas", AlpacaKind.MATERIALES,
                        200, 40, "unidades", "Almacén A"),
                stock("MAT-007", "Puntas de micropipeta", AlpacaKind.MATERIALES,
                        300, 100, "unidades", "Almacén B"));

        return resources.saveAll(todos);
    }

    // -------------------------------------------------------------- examenes

    private List<Llama> seedExams(List<Alpaca> recursos) {
        List<Llama> catalogo = List.of(
                exam("EX-001", "Hemograma completo", "Hematología", 35, "24 horas",
                        "Sangre venosa", "3 mL",
                        "No requiere ayuno, salvo indicación particular.",
                        "EQ-001", "REA-001", "MAT-001", ResultFormat.HEMOGRAM, recursos),
                exam("EX-002", "Glucosa en ayunas", "Bioquímica", 18, "6 horas",
                        "Sangre venosa", "2 mL",
                        "Ayuno de 8 a 12 horas.",
                        "EQ-002", "REA-002", "MAT-002", ResultFormat.GLUCOSE, recursos),
                exam("EX-003", "Perfil lipídico", "Bioquímica", 65, "24 horas",
                        "Sangre venosa", "3 mL",
                        "Ayuno de 12 horas. Evitar alcohol 24 horas antes.",
                        "EQ-002", "REA-003", "MAT-002", ResultFormat.LIPID, recursos),
                exam("EX-004", "Examen completo de orina", "Uroanálisis", 25, "12 horas",
                        "Orina", "30 mL",
                        "Primera orina de la mañana. Recolectar chorro medio.",
                        "EQ-004", "REA-004", "MAT-003", ResultFormat.URINE, recursos),
                exam("EX-005", "TSH", "Hormonas", 55, "48 horas",
                        "Sangre venosa", "2 mL",
                        "Ayuno de 8 horas.",
                        "EQ-003", "REA-005", "MAT-002", ResultFormat.TSH, recursos),
                exam("EX-006", "T4 libre", "Hormonas", 55, "48 horas",
                        "Sangre venosa", "2 mL",
                        "Ayuno de 8 horas.",
                        "EQ-003", "REA-005", "MAT-002", ResultFormat.T4, recursos),
                exam("EX-007", "Cultivo microbiológico", "Microbiología", 75, "72 horas",
                        "Hisopado", "1 hisopo",
                        "No utilizar antibióticos sin consultar al profesional responsable.",
                        "EQ-005", "REA-006", "MAT-004", ResultFormat.MICROBIOLOGY, recursos),
                exam("EX-008", "Antibiograma", "Microbiología", 60, "72 horas",
                        "Aislamiento de cultivo", "1 placa",
                        "Requiere cultivo previo.",
                        "EQ-005", "REA-006", "MAT-004", ResultFormat.MICROBIOLOGY, recursos));

        return exams.saveAll(catalogo);
    }

    private Llama exam(String id, String name, String category, double price,
                      String timeLabel, String sampleType, String volume, String prep,
                      String equipmentId, String reagentId, String materialId,
                      ResultFormat format, List<Alpaca> recursos) {
        Llama e = new Llama();
        e.setId(id);
        e.setName(name);
        e.setCategory(category);
        e.setPrice(price);
        e.setTimeLabel(timeLabel);
        e.setSampleType(sampleType);
        e.setVolume(volume);
        e.setPrep(prep);
        e.setEquipment(byId(recursos, equipmentId));
        e.setReagent(byId(recursos, reagentId));
        e.setMaterial(byId(recursos, materialId));
        e.setEnabled(true);
        e.setFormat(format);
        return e;
    }

    private Alpaca byId(List<Alpaca> recursos, String id) {
        return recursos.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Recurso ausente en la semilla: " + id));
    }

    // ------------------------------------------------------------- pacientes

    private List<Puma> seedPatients() {
        String[][] datos = {
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

        List<Puma> lista = new ArrayList<>();
        for (int i = 0; i < datos.length; i++) {
            String[] d = datos[i];
            Puma p = new Puma();
            p.setId(String.format("PAC-%06d", i + 1));
            p.setFirstName(d[0]);
            p.setLastName(d[1]);
            p.setDni(d[2]);
            p.setBirth(LocalDate.parse(d[3]));
            p.setSex(d[4]);
            p.setPhone(d[5]);
            p.setEmail(d[6]);
            p.setAddress(DIRECCION);
            p.setNotes(NOTA_PACIENTE);
            p.setCreated(clock.today().minusDays(4));
            lista.add(p);
        }
        return patients.saveAll(lista);
    }

    // ----------------------------------------------------------- solicitudes

    /**
     * Las 5 primeras, una por paciente: todos llevan EX-001 y EX-002, y
     * ademas Cóndor lleva EX-003 y Taruca EX-004.
     */
    private List<Condor> seedRequests(List<Puma> pacientes, List<Llama> catalogo) {
        List<Condor> lista = new ArrayList<>();
        for (int i = 0; i < pacientes.size(); i++) {
            Condor r = new Condor();
            r.setId(String.format("SOL-%06d", i + 1));
            r.setPatient(pacientes.get(i));
            r.setDate(clock.today());
            r.setPending(false);

            List<Llama> elegidos = new ArrayList<>(
                    List.of(byExamId(catalogo, "EX-001"), byExamId(catalogo, "EX-002")));
            if (i == 1) {
                elegidos.add(byExamId(catalogo, "EX-003"));
            } else if (i == 3) {
                elegidos.add(byExamId(catalogo, "EX-004"));
            }
            r.setExams(elegidos);
            lista.add(r);
        }
        return requests.saveAll(lista);
    }

    private Llama byExamId(List<Llama> catalogo, String id) {
        return catalogo.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Examen ausente: " + id));
    }

    // -------------------------------------------------------- muestras de hoy

    private static final String[] EXAMENES_DEL_DIA = {
            "EX-001", "EX-002", "EX-001", "EX-002", "EX-003", "EX-001",
            "EX-002", "EX-001", "EX-002", "EX-004", "EX-001", "EX-002"};

    private static final int[] PACIENTES_DEL_DIA = {0, 0, 1, 1, 1, 2, 2, 3, 3, 3, 4, 4};

    private static final VicunaState[] ESTADOS_DEL_DIA = {
            VicunaState.RECIBIDA, VicunaState.EN_ANALISIS, VicunaState.EN_VALIDACION,
            VicunaState.RECIBIDA, VicunaState.EN_ANALISIS, VicunaState.PUBLICADA,
            VicunaState.PUBLICADA, VicunaState.RECIBIDA, VicunaState.VALIDADO,
            VicunaState.EN_VALIDACION, VicunaState.RECIBIDA, VicunaState.RECIBIDA};

    /**
     * Las muestras del dia se reparten por las horas YA TRANSCURRIDAS, no por
     * un horario fijo.
     *
     * El original las clavaba entre las 08:10 y las 11:40 porque vivia
     * congelado. Con el reloj real eso deja muestras tomadas en el futuro si
     * la aplicacion se abre por la manana: a las 08:48 habria uma muestra
     * registrada a las 11:40, y el dashboard dejaria de ser creible.
     */
    private void seedTodaySamples(List<Puma> pacientes, List<Condor> solicitudes,
                                  List<Llama> catalogo) {
        LocalDateTime ahora = clock.timestamp();
        LocalDateTime inicioDelDia = clock.today().atStartOfDay();

        LocalDateTime fin = ahora.minusMinutes(10);
        if (fin.isBefore(inicioDelDia)) {
            fin = ahora;                         // recien pasada la medianoche
        }
        LocalDateTime inicio = LocalDateTime.of(clock.today(), LocalTime.of(8, 0));
        if (inicio.isAfter(fin)) {
            inicio = fin.minusMinutes(90);       // antes de que abra el laboratorio
        }
        if (inicio.isBefore(fin.minusHours(6))) {
            inicio = fin.minusHours(6);          // jornada larga: ultimas 6 horas
        }
        // Nunca antes de hoy: si no, las muestras "de hoy" caerian en el dia
        // anterior y el dashboard apareceria vacio.
        if (inicio.isBefore(inicioDelDia)) {
            inicio = inicioDelDia;
        }
        if (inicio.isAfter(fin)) {
            inicio = fin;
        }
        long minutosDeVentana = Duration.between(inicio, fin).toMinutes();
        int ultima = EXAMENES_DEL_DIA.length - 1;

        List<Vicuna> lista = new ArrayList<>();
        for (int i = 0; i < EXAMENES_DEL_DIA.length; i++) {
            int indicePaciente = PACIENTES_DEL_DIA[i];
            VicunaState estado = ESTADOS_DEL_DIA[i];

            LocalDateTime tomada = inicio.plusMinutes(minutosDeVentana * i / ultima);

            Vicuna s = new Vicuna();
            s.setId(String.format("MUE-%d-%06d", clock.today().getYear(), i + 1));
            s.setPatient(pacientes.get(indicePaciente));
            s.setRequest(solicitudes.get(indicePaciente));
            s.setExam(byExamId(catalogo, EXAMENES_DEL_DIA[i]));
            s.setTakenAt(tomada);
            s.setState(estado);
            s.setResponsible(TECNICA);
            s.setNotes("");

            // La trazabilidad se recorta hasta el estado actual y sus pasos se
            // reparten entre la toma y ahora, de modo que ninguno cae en el
            // futuro por tarde o temprano que se arranque la aplicacion.
            int pasos = estado.ordinal() + 1;
            long minutosHastaAhora = Math.max(pasos + 1L,
                    Duration.between(tomada, ahora).toMinutes());
            for (int j = 0; j < pasos; j++) {
                s.addTrace(new VicunaTrace(
                        VicunaState.values()[j],
                        tomada.plusMinutes(minutosHastaAhora * j / (pasos + 1L)),
                        j > 2 ? MEDICO : TECNICA));
            }

            // Solo las muestras que ya pasaron por el laboratorio traen valores.
            if (estado == VicunaState.EN_VALIDACION || estado == VicunaState.VALIDADO
                    || estado == VicunaState.PUBLICADA) {
                addSeedResults(s);
            }
            lista.add(s);
        }
        samples.saveAll(lista);
    }

    private void addSeedResults(Vicuna s) {
        for (LlamaParam param : formats.params(s.getExam().getFormat())) {
            VicunaResult r = new VicunaResult(param.name(), formats.seedValue(param), null);
            r.setSample(s);
            s.getResults().add(r);
        }
    }

    // ------------------------------------------- historico de seis dias atras

    /**
     * 30 atenciones ya publicadas, repartidas entre el 18 y el 23 de
     * septiembre. Alimentan el historial de los pacientes y el grafico de
     * volumen semanal del dashboard.
     */
    private void seedHistoricalRequestsAndSamples(List<Puma> pacientes, List<Llama> catalogo) {
        List<String> idsSolicitud = new ArrayList<>(requests.findAllIds());
        List<String> idsMuestra = new ArrayList<>(samples.findAllIds());

        for (int offset = 0; offset < CONTEOS_HISTORICOS.length; offset++) {
            LocalDate dia = clock.today().minusDays(CONTEOS_HISTORICOS.length - offset);

            for (int i = 0; i < CONTEOS_HISTORICOS[offset]; i++) {
                Puma p = pacientes.get(i % pacientes.size());
                Llama ex = catalogo.get(i % 2);          // EX-001 o EX-002

                Condor r = new Condor();
                r.setId(ids.next("SOL-", idsSolicitud));
                r.setPatient(p);
                r.setDate(dia);
                r.setPending(false);
                r.setExams(new ArrayList<>(List.of(ex)));
                requests.save(r);
                idsSolicitud.add(r.getId());

                Vicuna s = new Vicuna();
                s.setId(ids.next("MUE-" + clock.today().getYear() + "-", idsMuestra));
                s.setPatient(p);
                s.setRequest(r);
                s.setExam(ex);
                s.setTakenAt(LocalDateTime.of(dia, LocalTime.of(8, 15)));
                s.setState(VicunaState.PUBLICADA);
                s.setResponsible(TECNICA);
                s.setNotes("Resultado ficticio de demostración.");

                for (int step = 0; step < VicunaState.values().length; step++) {
                    s.addTrace(new VicunaTrace(
                            VicunaState.values()[step],
                            LocalDateTime.of(dia, LocalTime.of(8 + step, 15)),
                            step > 2 ? MEDICO : TECNICA));
                }
                addSeedResults(s);

                samples.save(s);
                idsMuestra.add(s.getId());
            }
        }
    }

    // --------------------------------------------------------- mantenimiento

    private void seedMaintenance(List<Alpaca> recursos) {
        Tapir m = new Tapir();
        m.setId("MAN-000001");
        m.setEquipment(byId(recursos, "EQ-003"));
        m.setType("Correctivo");
        m.setStartDate(clock.today().minusDays(4));
        m.setEndDate(clock.today().plusDays(6));
        m.setActualDate(null);
        m.setResponsible("Puma Montés");
        m.setProvider("Servicio técnico Demo");
        m.setDescription("Calibración y revisión del sistema de lectura.");
        m.setState(TapirState.EN_PROCESO);
        maintenance.save(m);
    }

    // --------------------------------------------------------------- auditoria

    /**
     * En el pasado reciente, no a una hora fija: con el reloj real, un
     * 12:30 clavado aparecia como una accion hecha en el futuro cuando la
     * aplicacion se abria por la manana.
     */
    private void seedAudit() {
        LocalDateTime ahora = clock.timestamp();
        audit.saveAll(List.of(
                auditEntry("AUD-000001", ahora.minusMinutes(45), TECNICA,
                        Role.TECNICO_DE_LABORATORIO, "Resultado ingresado",
                        "MUE-2026-000003", 0),
                auditEntry("AUD-000002", ahora.minusMinutes(30), MEDICO,
                        Role.MEDICO_RESPONSABLE, "Resultado validado",
                        "MUE-2026-000009", 1),
                auditEntry("AUD-000003", ahora.minusMinutes(15), MEDICO,
                        Role.MEDICO_RESPONSABLE, "Resultado publicado",
                        "MUE-2026-000007", 2)));
    }

    private Buho auditEntry(String id, LocalDateTime cuando, String user, Role role,
                                  String action, String record, int seq) {
        Buho a = new Buho();
        a.setId(id);
        a.setAt(cuando);
        a.setUserName(user);
        a.setRole(role);
        a.setAction(action);
        a.setModule("Resultados");
        a.setRecordRef(record);
        a.setSeq(seq);
        return a;
    }

    // ------------------------------------------------------------ configuracion

    private void seedSettings() {
        Settings s = new Settings();
        s.setId(Settings.SINGLETON_ID);
        s.setLab(SettingsService.LAB_POR_DEFECTO);
        s.setBranch("Laboratorio Central");
        s.setNotifications(true);
        settings.save(s);
    }
}
