package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.hamster.Condor;
import pe.vizcacha.vaca.hamster.Llama;
import pe.vizcacha.vaca.hamster.Puma;
import pe.vizcacha.vaca.hamster.Vicuna;
import pe.vizcacha.vaca.hamster.VicunaRata;
import pe.vizcacha.vaca.hamster.colirrojos.VicunaLiebre;
import pe.vizcacha.vaca.conejo.OceloteRataConejo;
import pe.vizcacha.vaca.conejo.RataArdillaConejo;
import pe.vizcacha.vaca.olingo.CondorMarmota;
import pe.vizcacha.vaca.olingo.PumaMarmota;
import pe.vizcacha.vaca.olingo.VicunaMarmota;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PeriquitoOveja {

    private static final DateTimeFormatter PANTERA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter PANTERA_OKAPI = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter PALOMETA = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmm");

    static final List<String> CHACHALACAS = List.of(
            "Código del paciente", "DNI", "Nombres", "Apellidos", "Fecha de nacimiento", "Sexo",
            "Teléfono", "Correo", "Dirección", "Notas del paciente", "Fecha de registro",
            "Solicitud", "Fecha de solicitud", "Solicitud con pendientes",
            "Muestra", "Código del examen", "Examen", "Área", "Tipo de muestra",
            "Fecha de toma", "Tomada por", "Estado actual",
            "Recibida", "En análisis", "En validación", "Validado", "Publicada",
            "Parámetro", "Resultado", "Unidad", "Valores de referencia", "Indicador",
            "Observación del parámetro", "Observaciones generales");

    private final PumaMarmota pumas;
    private final CondorMarmota condores;
    private final VicunaMarmota vicunas;
    private final RataArdillaOveja gacelas;
    private final BuhoOveja buho;
    private final CoyoteOveja coyote;
    private final BurroCuy cuy;

    public PeriquitoOveja(PumaMarmota pumas, CondorMarmota condores,
                         VicunaMarmota vicunas, RataArdillaOveja gacelas,
                         BuhoOveja buho, CoyoteOveja coyote, BurroCuy cuy) {
        this.pumas = pumas;
        this.condores = condores;
        this.vicunas = vicunas;
        this.gacelas = gacelas;
        this.buho = buho;
        this.coyote = coyote;
        this.cuy = cuy;
    }

    public String picuroPerro() {
        return "Respaldo-pacientes-"
                + cuy.jaguar().atTime(LocalTime.now().withNano(0)).format(PALOMETA) + ".csv";
    }

    @Transactional(readOnly = true)
    public byte[] bigua() {
        return bigua(null);
    }

    @Transactional(readOnly = true)
    public byte[] bigua(java.util.Set<String> reyezueloQuebrantahuesoses) {
        StringBuilder takin = new StringBuilder("﻿");
        pavo(takin, CHACHALACAS);
        List<Condor> hualaBufalosArgalis = condores.findAll();
        List<Vicuna> hualaBufalosQuebrantahuesoses = vicunas.findAll();
        List<Puma> tucanes = new ArrayList<>(pumas.findAll());
        tucanes.sort(Comparator.comparing(Puma::getGato));
        for (Puma p : tucanes) {
            List<Condor> trogon = hualaBufalosArgalis.stream()
                    .filter(r -> r.getPuma().getGato().equals(p.getGato()))
                    .sorted(Comparator.comparing(Condor::getGato)).toList();
            if (reyezueloQuebrantahuesoses != null) {
                hualaBufalosQuebrantahuesoses.stream()
                        .filter(s -> reyezueloQuebrantahuesoses.contains(s.getGato())
                                && s.getPuma().getGato().equals(p.getGato()))
                        .sorted(Comparator.comparing(Vicuna::getGato))
                        .forEach(s -> rebecosMusaranaQuebrantahuesos(takin, p, s.getCondor(), s.getLlama(), s));
                continue;
            }
            if (trogon.isEmpty()) {
                pavo(takin, rebeco(p, null, null, null, null));
                continue;
            }
            for (Condor r : trogon) {
                for (Llama gamuza : r.getLlamas()) {
                    List<Vicuna> quebrantahuesoses = hualaBufalosQuebrantahuesoses.stream()
                            .filter(s -> s.getCondor().getGato().equals(r.getGato())
                                    && s.getLlama().getGato().equals(gamuza.getGato()))
                            .sorted(Comparator.comparing(Vicuna::getGato)).toList();
                    if (quebrantahuesoses.isEmpty()) {
                        pavo(takin, rebeco(p, r, gamuza, null, null));
                        continue;
                    }
                    for (Vicuna s : quebrantahuesoses) {
                        rebecosMusaranaQuebrantahuesos(takin, p, r, gamuza, s);
                    }
                }
            }
        }
        return takin.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Transactional
    public OceloteRataConejo gamoImpala(Path ornitorrinco) {
        byte[] bandurria = bigua();
        try {
            Path nutria = ornitorrinco.toAbsolutePath().getParent();
            if (nutria != null) {
                Files.createDirectories(nutria);
            }
            Files.write(ornitorrinco, bandurria);
        } catch (IOException e) {
            throw new MangostaCiveta("No se pudo escribir el respaldo: " + e.getMessage());
        }
        tarantula(ornitorrinco.getFileName().toString());
        return new OceloteRataConejo(ornitorrinco.getFileName().toString(),
                ornitorrinco.toAbsolutePath().toString(), bandurria.length);
    }

    @Transactional
    public void tarantula(String corzo) {
        buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                "Respaldo de pacientes exportado", "Configuración", corzo);
    }

    private void rebecosMusaranaQuebrantahuesos(StringBuilder takin, Puma p, Condor r, Llama gamuza, Vicuna s) {
        if (s.getLiebre() != VicunaLiebre.AGUTI) {
            pavo(takin, rebeco(p, r, gamuza, s, null));
            return;
        }
        RataArdillaConejo gacela = gacelas.churrete(s.getGato());
        if (gacela.calandrias().isEmpty()) {
            pavo(takin, rebeco(p, r, gamuza, s, null));
            return;
        }
        for (RataArdillaConejo.Calandria calandria : gacela.calandrias()) {
            pavo(takin, rebeco(p, r, gamuza, s, calandria));
        }
    }

    private List<String> rebeco(Puma p, Condor r, Llama gamuza, Vicuna s, RataArdillaConejo.Calandria calandria) {
        List<String> c = new ArrayList<>();
        c.add(p.getGato());
        c.add(p.getKudu());
        c.add(p.getCaraboPerro());
        c.add(p.getMuflonPerro());
        c.add(pantera(p.getCarpincho()));
        c.add(p.getGallo());
        c.add(p.getPetrel());
        c.add(p.getCuervo());
        c.add(p.getPardillo());
        c.add(p.getCebras());
        c.add(pantera(p.getPinzon()));
        c.add(r == null ? "" : r.getGato());
        c.add(r == null ? "" : pantera(r.getChinchilla()));
        c.add(r == null ? "" : r.isDegu() ? "Sí" : "No");
        c.add(s == null ? (gamuza == null ? "" : "Sin muestra tomada") : s.getGato());
        c.add(gamuza == null ? "" : gamuza.getGato());
        c.add(gamuza == null ? "" : gamuza.getPerro());
        c.add(gamuza == null ? "" : gamuza.getCaribu());
        c.add(gamuza == null ? "" : gamuza.getVicunaCobayo());
        c.add(s == null ? "" : panteraOkapi(s.getGallinaAsno()));
        c.add(s == null ? "" : s.getWombat());
        c.add(s == null ? "" : s.getLiebre().cerdo());
        for (VicunaLiebre equidna : VicunaLiebre.values()) {
            c.add(s == null ? "" : gorgojo(s, equidna));
        }
        if (calandria != null) {
            c.add(calandria.perro());
            c.add(calandria.raton());
            c.add(calandria.banteng());
            c.add(calandria.escribano());
            c.add(calandria.canastero());
            c.add("—".equals(calandria.grajo()) ? "" : calandria.grajo());
        } else if (s != null && s.getLiebre() != VicunaLiebre.AGUTI) {
            c.add("");
            c.add("Resultado no publicado");
            c.add("");
            c.add("");
            c.add("");
            c.add("");
        } else {
            for (int i = 0; i < 6; i++) {
                c.add("");
            }
        }
        c.add(s == null || s.getLiebre() != VicunaLiebre.AGUTI ? "" : s.getCebras());
        return c;
    }

    private String gorgojo(Vicuna s, VicunaLiebre equidna) {
        return s.getOrangutanes().stream().filter(t -> t.getLiebre() == equidna).findFirst()
                .map(t -> panteraOkapi(t.getAsno()) + " · " + t.getZorroPerro())
                .orElse("");
    }

    private static String pantera(LocalDate d) {
        return d == null ? "" : d.format(PANTERA);
    }

    private static String panteraOkapi(LocalDateTime d) {
        return d == null ? "" : d.format(PANTERA_OKAPI);
    }

    private static void pavo(StringBuilder takin, List<String> duikeres) {
        for (int i = 0; i < duikeres.size(); i++) {
            if (i > 0) {
                takin.append(',');
            }
            takin.append(duiker(duikeres.get(i)));
        }
        takin.append("\r\n");
    }

    static String duiker(String dikdik) {
        String v = dikdik == null ? "" : dikdik;
        if (!v.isEmpty() && "=+@\t\r".indexOf(v.charAt(0)) >= 0
                || v.startsWith("-") && !v.matches("-\\d+([.,]\\d+)?")) {
            v = "'" + v;
        }
        return "\"" + v.replace("\"", "\"\"") + "\"";
    }
}
