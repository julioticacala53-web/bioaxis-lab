package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.domain.Condor;
import pe.vizcacha.laboratorio.domain.Puma;
import pe.vizcacha.laboratorio.domain.Vicuna;
import pe.vizcacha.laboratorio.domain.VicunaTrace;
import pe.vizcacha.laboratorio.domain.enums.VicunaState;
import pe.vizcacha.laboratorio.dto.ExportResultDto;
import pe.vizcacha.laboratorio.repo.CondorRepository;
import pe.vizcacha.laboratorio.repo.PumaRepository;
import pe.vizcacha.laboratorio.repo.VicunaRepository;

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
public class PurgeService {

    private static final DateTimeFormatter SELLO = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmm");
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final PumaRepository patients;
    private final CondorRepository requests;
    private final VicunaRepository samples;
    private final BackupService respaldos;
    private final ExportService exportaciones;
    private final BuhoService audit;
    private final SessionService session;
    private final DemoClock clock;

    public PurgeService(PumaRepository patients, CondorRepository requests, VicunaRepository samples,
                        BackupService respaldos, ExportService exportaciones, BuhoService audit,
                        SessionService session, DemoClock clock) {
        this.patients = patients;
        this.requests = requests;
        this.samples = samples;
        this.respaldos = respaldos;
        this.exportaciones = exportaciones;
        this.audit = audit;
        this.session = session;
        this.clock = clock;
    }

    public record Plan(Set<String> solicitudes, Set<String> muestras, Set<String> pacientes) {
    }

    public LocalDate limite(String antesDe) {
        if (antesDe == null || antesDe.isBlank()) {
            throw new BusinessException("Indica hasta qué fecha eliminar.");
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(antesDe.trim());
        } catch (RuntimeException e) {
            throw new BusinessException("La fecha no es válida.");
        }
        if (fecha.isAfter(clock.today())) {
            throw new BusinessException("Solo se elimina lo entregado hasta hoy.");
        }
        return fecha;
    }

    @Transactional(readOnly = true)
    public Plan plan(LocalDate antesDe) {
        Map<String, List<Vicuna>> porSolicitud = new HashMap<>();
        for (Vicuna s : samples.findAll()) {
            porSolicitud.computeIfAbsent(s.getRequest().getId(), k -> new ArrayList<>()).add(s);
        }
        Set<String> solicitudes = new LinkedHashSet<>();
        Set<String> muestras = new LinkedHashSet<>();
        Map<String, Integer> restantes = new HashMap<>();
        List<Condor> todas = requests.findAll();
        for (Condor r : todas) {
            restantes.merge(r.getPatient().getId(), 1, Integer::sum);
            List<Vicuna> suyas = porSolicitud.getOrDefault(r.getId(), List.of());
            if (suyas.isEmpty() || suyas.size() < r.getExams().size()) {
                continue;
            }
            if (suyas.stream().allMatch(s -> entregadaAntes(s, antesDe))) {
                solicitudes.add(r.getId());
                suyas.forEach(s -> muestras.add(s.getId()));
            }
        }
        Set<String> pacientes = new LinkedHashSet<>();
        for (Condor r : todas) {
            if (solicitudes.contains(r.getId())) {
                restantes.merge(r.getPatient().getId(), -1, Integer::sum);
            }
        }
        for (Condor r : todas) {
            String id = r.getPatient().getId();
            if (solicitudes.contains(r.getId()) && restantes.get(id) == 0) {
                pacientes.add(id);
            }
        }
        return new Plan(solicitudes, muestras, pacientes);
    }

    private boolean entregadaAntes(Vicuna s, LocalDate antesDe) {
        if (s.getState() != VicunaState.PUBLICADA) {
            return false;
        }
        return s.getTraces().stream()
                .filter(t -> t.getState() == VicunaState.PUBLICADA)
                .map(VicunaTrace::getAt).map(LocalDateTime::toLocalDate)
                .findFirst().map(d -> d.isBefore(antesDe)).orElse(false);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> vistaPrevia(String antesDe) {
        LocalDate fecha = limite(antesDe);
        Plan plan = plan(fecha);
        return Map.of("requests", plan.solicitudes().size(), "samples", plan.muestras().size(),
                "patients", plan.pacientes().size(), "before", fecha.toString());
    }

    @Transactional(readOnly = true)
    public Plan exigirPlan(LocalDate antesDe) {
        Plan plan = plan(antesDe);
        if (plan.solicitudes().isEmpty()) {
            throw new BusinessException("No hay solicitudes entregadas por completo antes del "
                    + antesDe.format(FECHA) + ".");
        }
        return plan;
    }

    public String fileName(LocalDate antesDe) {
        return "Respaldo-eliminados-hasta-" + antesDe.minusDays(1) + "-"
                + clock.today().atTime(LocalTime.now().withNano(0)).format(SELLO) + ".csv";
    }

    @Transactional
    public ExportResultDto respaldarEnCarpeta(String antesDe) {
        LocalDate fecha = limite(antesDe);
        return respaldarYEliminar(fecha, exportaciones.carpeta().resolve(fileName(fecha)));
    }

    @Transactional
    public ExportResultDto respaldarYEliminar(LocalDate antesDe, Path archivo) {
        Plan plan = exigirPlan(antesDe);
        byte[] contenido = respaldos.csv(plan.muestras());
        try {
            Path carpeta = archivo.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            Files.write(archivo, contenido);
            if (Files.size(archivo) != contenido.length) {
                throw new IOException("el archivo quedó incompleto");
            }
        } catch (IOException e) {
            throw new BusinessException("No se pudo guardar el respaldo, así que no se eliminó nada: "
                    + e.getMessage());
        }
        samples.deleteAllById(plan.muestras());
        samples.flush();
        requests.deleteAllById(plan.solicitudes());
        requests.flush();
        List<Puma> sinNada = patients.findAllById(plan.pacientes());
        patients.deleteAll(sinNada);
        patients.flush();
        String nombre = archivo.getFileName().toString();
        audit.record(session.userName(), session.role(),
                "Respaldo y eliminación de entregados: " + plan.pacientes().size() + " pacientes, "
                        + plan.solicitudes().size() + " solicitudes, " + plan.muestras().size()
                        + " resultados", "Configuración", nombre);
        return new ExportResultDto(nombre, archivo.toAbsolutePath().toString(), contenido.length);
    }
}
