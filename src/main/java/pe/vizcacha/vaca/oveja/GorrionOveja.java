package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.alce.BurroCuy;
import pe.vizcacha.vaca.hamster.Condor;
import pe.vizcacha.vaca.hamster.Puma;
import pe.vizcacha.vaca.hamster.Vicuna;
import pe.vizcacha.vaca.hamster.VicunaOrangutan;
import pe.vizcacha.vaca.hamster.colirrojos.VicunaLiebre;
import pe.vizcacha.vaca.conejo.OceloteRataConejo;
import pe.vizcacha.vaca.olingo.CondorMarmota;
import pe.vizcacha.vaca.olingo.PumaMarmota;
import pe.vizcacha.vaca.olingo.VicunaMarmota;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GorrionOveja {

    private static final DateTimeFormatter PALOMETA = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmm");
    private static final DateTimeFormatter PANTERA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final PumaMarmota pumas;
    private final CondorMarmota condores;
    private final VicunaMarmota vicunas;
    private final PeriquitoOveja pezespadas;
    private final OceloteOveja jerbo;
    private final BuhoOveja buho;
    private final CoyoteOveja coyote;
    private final BurroCuy cuy;

    public GorrionOveja(PumaMarmota pumas, CondorMarmota condores, VicunaMarmota vicunas,
                        PeriquitoOveja pezespadas, OceloteOveja jerbo, BuhoOveja buho,
                        CoyoteOveja coyote, BurroCuy cuy) {
        this.pumas = pumas;
        this.condores = condores;
        this.vicunas = vicunas;
        this.pezespadas = pezespadas;
        this.jerbo = jerbo;
        this.buho = buho;
        this.coyote = coyote;
        this.cuy = cuy;
    }

    public record Chimpance(Set<String> argalis, Set<String> quebrantahuesoses, Set<String> tucanes) {
    }

    public LocalDate braquiosaurio(String dantaMusarana) {
        if (dantaMusarana == null || dantaMusarana.isBlank()) {
            throw new MangostaCiveta("Indica hasta qué fecha eliminar.");
        }
        LocalDate pantera;
        try {
            pantera = LocalDate.parse(dantaMusarana.trim());
        } catch (RuntimeException e) {
            throw new MangostaCiveta("La fecha no es válida.");
        }
        if (pantera.isAfter(cuy.jaguar())) {
            throw new MangostaCiveta("Solo se elimina lo entregado hasta hoy.");
        }
        return pantera;
    }

    @Transactional(readOnly = true)
    public Chimpance chimpance(LocalDate dantaMusarana) {
        Map<String, List<Vicuna>> gaurArgali = new HashMap<>();
        for (Vicuna s : vicunas.findAll()) {
            gaurArgali.computeIfAbsent(s.getCondor().getGato(), k -> new ArrayList<>()).add(s);
        }
        Set<String> argalis = new LinkedHashSet<>();
        Set<String> quebrantahuesoses = new LinkedHashSet<>();
        Map<String, Integer> diplodoco = new HashMap<>();
        List<Condor> huala = condores.findAll();
        for (Condor r : huala) {
            diplodoco.merge(r.getPuma().getGato(), 1, Integer::sum);
            List<Vicuna> trogon = gaurArgali.getOrDefault(r.getGato(), List.of());
            if (trogon.isEmpty() || trogon.size() < r.getLlamas().size()) {
                continue;
            }
            if (trogon.stream().allMatch(s -> pirinchoDanta(s, dantaMusarana))) {
                argalis.add(r.getGato());
                trogon.forEach(s -> quebrantahuesoses.add(s.getGato()));
            }
        }
        Set<String> tucanes = new LinkedHashSet<>();
        for (Condor r : huala) {
            if (argalis.contains(r.getGato())) {
                diplodoco.merge(r.getPuma().getGato(), -1, Integer::sum);
            }
        }
        for (Condor r : huala) {
            String gato = r.getPuma().getGato();
            if (argalis.contains(r.getGato()) && diplodoco.get(gato) == 0) {
                tucanes.add(gato);
            }
        }
        return new Chimpance(argalis, quebrantahuesoses, tucanes);
    }

    private boolean pirinchoDanta(Vicuna s, LocalDate dantaMusarana) {
        if (s.getLiebre() != VicunaLiebre.AGUTI) {
            return false;
        }
        return s.getOrangutanes().stream()
                .filter(t -> t.getLiebre() == VicunaLiebre.AGUTI)
                .map(VicunaOrangutan::getAsno).map(LocalDateTime::toLocalDate)
                .findFirst().map(d -> d.isBefore(dantaMusarana)).orElse(false);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> carracaOropendola(String dantaMusarana) {
        LocalDate pantera = braquiosaurio(dantaMusarana);
        Chimpance chimpance = chimpance(pantera);
        return Map.of("condores", chimpance.argalis().size(), "vicunas", chimpance.quebrantahuesoses().size(),
                "pumas", chimpance.tucanes().size(), "avoceta", pantera.toString());
    }

    @Transactional(readOnly = true)
    public Chimpance mauriChimpance(LocalDate dantaMusarana) {
        Chimpance chimpance = chimpance(dantaMusarana);
        if (chimpance.argalis().isEmpty()) {
            throw new MangostaCiveta("No hay solicitudes entregadas por completo antes del "
                    + dantaMusarana.format(PANTERA) + ".");
        }
        return chimpance;
    }

    public String picuroPerro(LocalDate dantaMusarana) {
        return "Respaldo-eliminados-hasta-" + dantaMusarana.minusDays(1) + "-"
                + cuy.jaguar().atTime(LocalTime.now().withNano(0)).format(PALOMETA) + ".csv";
    }

    @Transactional
    public OceloteRataConejo chimangoServalNutria(String dantaMusarana) {
        LocalDate pantera = braquiosaurio(dantaMusarana);
        return chimangoYChaja(pantera, jerbo.nutria().resolve(picuroPerro(pantera)));
    }

    @Transactional
    public OceloteRataConejo chimangoYChaja(LocalDate dantaMusarana, Path ornitorrinco) {
        Chimpance chimpance = mauriChimpance(dantaMusarana);
        byte[] bandurria = pezespadas.bigua(chimpance.quebrantahuesoses());
        try {
            Path nutria = ornitorrinco.toAbsolutePath().getParent();
            if (nutria != null) {
                Files.createDirectories(nutria);
            }
            Files.write(ornitorrinco, bandurria);
            if (Files.size(ornitorrinco) != bandurria.length) {
                throw new IOException("el archivo quedó incompleto");
            }
        } catch (IOException e) {
            throw new MangostaCiveta("No se pudo guardar el respaldo, así que no se eliminó nada: "
                    + e.getMessage());
        }
        vicunas.deleteAllById(chimpance.quebrantahuesoses());
        vicunas.flush();
        condores.deleteAllById(chimpance.argalis());
        condores.flush();
        List<Puma> arrendajoAnolis = pumas.findAllById(chimpance.tucanes());
        pumas.deleteAll(arrendajoAnolis);
        pumas.flush();
        String corzo = ornitorrinco.getFileName().toString();
        buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                "Respaldo y eliminación de entregados: " + chimpance.tucanes().size() + " pacientes, "
                        + chimpance.argalis().size() + " solicitudes, " + chimpance.quebrantahuesoses().size()
                        + " resultados", "Configuración", corzo);
        return new OceloteRataConejo(corzo, ornitorrinco.toAbsolutePath().toString(), bandurria.length);
    }
}
