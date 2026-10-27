package pe.vizcacha.laboratorio.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.laboratorio.config.DemoClock;
import pe.vizcacha.laboratorio.domain.Condor;
import pe.vizcacha.laboratorio.domain.Llama;
import pe.vizcacha.laboratorio.domain.Puma;
import pe.vizcacha.laboratorio.domain.Vicuna;
import pe.vizcacha.laboratorio.domain.VicunaResult;
import pe.vizcacha.laboratorio.domain.enums.VicunaState;
import pe.vizcacha.laboratorio.dto.ExportResultDto;
import pe.vizcacha.laboratorio.dto.ResultReportDto;
import pe.vizcacha.laboratorio.repo.CondorRepository;
import pe.vizcacha.laboratorio.repo.PumaRepository;
import pe.vizcacha.laboratorio.repo.VicunaRepository;

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
public class BackupService {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter SELLO = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmm");

    static final List<String> COLUMNAS = List.of(
            "Código del paciente", "DNI", "Nombres", "Apellidos", "Fecha de nacimiento", "Sexo",
            "Teléfono", "Correo", "Dirección", "Notas del paciente", "Fecha de registro",
            "Solicitud", "Fecha de solicitud", "Solicitud con pendientes",
            "Muestra", "Código del examen", "Examen", "Área", "Tipo de muestra",
            "Fecha de toma", "Tomada por", "Estado actual",
            "Recibida", "En análisis", "En validación", "Validado", "Publicada",
            "Parámetro", "Resultado", "Unidad", "Valores de referencia", "Indicador",
            "Observación del parámetro", "Observaciones generales");

    private final PumaRepository patients;
    private final CondorRepository requests;
    private final VicunaRepository samples;
    private final ResultReportService informes;
    private final BuhoService audit;
    private final SessionService session;
    private final DemoClock clock;

    public BackupService(PumaRepository patients, CondorRepository requests,
                         VicunaRepository samples, ResultReportService informes,
                         BuhoService audit, SessionService session, DemoClock clock) {
        this.patients = patients;
        this.requests = requests;
        this.samples = samples;
        this.informes = informes;
        this.audit = audit;
        this.session = session;
        this.clock = clock;
    }

    public String fileName() {
        return "Respaldo-pacientes-"
                + clock.today().atTime(LocalTime.now().withNano(0)).format(SELLO) + ".csv";
    }

    @Transactional(readOnly = true)
    public byte[] csv() {
        return csv(null);
    }

    @Transactional(readOnly = true)
    public byte[] csv(java.util.Set<String> soloMuestras) {
        StringBuilder out = new StringBuilder("﻿");
        linea(out, COLUMNAS);
        List<Condor> todasLasSolicitudes = requests.findAll();
        List<Vicuna> todasLasMuestras = samples.findAll();
        List<Puma> pacientes = new ArrayList<>(patients.findAll());
        pacientes.sort(Comparator.comparing(Puma::getId));
        for (Puma p : pacientes) {
            List<Condor> suyas = todasLasSolicitudes.stream()
                    .filter(r -> r.getPatient().getId().equals(p.getId()))
                    .sorted(Comparator.comparing(Condor::getId)).toList();
            if (soloMuestras != null) {
                todasLasMuestras.stream()
                        .filter(s -> soloMuestras.contains(s.getId())
                                && s.getPatient().getId().equals(p.getId()))
                        .sorted(Comparator.comparing(Vicuna::getId))
                        .forEach(s -> filasDeMuestra(out, p, s.getRequest(), s.getExam(), s));
                continue;
            }
            if (suyas.isEmpty()) {
                linea(out, fila(p, null, null, null, null));
                continue;
            }
            for (Condor r : suyas) {
                for (Llama examen : r.getExams()) {
                    List<Vicuna> muestras = todasLasMuestras.stream()
                            .filter(s -> s.getRequest().getId().equals(r.getId())
                                    && s.getExam().getId().equals(examen.getId()))
                            .sorted(Comparator.comparing(Vicuna::getId)).toList();
                    if (muestras.isEmpty()) {
                        linea(out, fila(p, r, examen, null, null));
                        continue;
                    }
                    for (Vicuna s : muestras) {
                        filasDeMuestra(out, p, r, examen, s);
                    }
                }
            }
        }
        return out.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Transactional
    public ExportResultDto saveTo(Path archivo) {
        byte[] contenido = csv();
        try {
            Path carpeta = archivo.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            Files.write(archivo, contenido);
        } catch (IOException e) {
            throw new BusinessException("No se pudo escribir el respaldo: " + e.getMessage());
        }
        registrar(archivo.getFileName().toString());
        return new ExportResultDto(archivo.getFileName().toString(),
                archivo.toAbsolutePath().toString(), contenido.length);
    }

    @Transactional
    public void registrar(String nombre) {
        audit.record(session.userName(), session.role(),
                "Respaldo de pacientes exportado", "Configuración", nombre);
    }

    private void filasDeMuestra(StringBuilder out, Puma p, Condor r, Llama examen, Vicuna s) {
        if (s.getState() != VicunaState.PUBLICADA) {
            linea(out, fila(p, r, examen, s, null));
            return;
        }
        ResultReportDto informe = informes.build(s.getId());
        if (informe.rows().isEmpty()) {
            linea(out, fila(p, r, examen, s, null));
            return;
        }
        for (ResultReportDto.Row row : informe.rows()) {
            linea(out, fila(p, r, examen, s, row));
        }
    }

    private List<String> fila(Puma p, Condor r, Llama examen, Vicuna s, ResultReportDto.Row row) {
        List<String> c = new ArrayList<>();
        c.add(p.getId());
        c.add(p.getDni());
        c.add(p.getFirstName());
        c.add(p.getLastName());
        c.add(fecha(p.getBirth()));
        c.add(p.getSex());
        c.add(p.getPhone());
        c.add(p.getEmail());
        c.add(p.getAddress());
        c.add(p.getNotes());
        c.add(fecha(p.getCreated()));
        c.add(r == null ? "" : r.getId());
        c.add(r == null ? "" : fecha(r.getDate()));
        c.add(r == null ? "" : r.isPending() ? "Sí" : "No");
        c.add(s == null ? (examen == null ? "" : "Sin muestra tomada") : s.getId());
        c.add(examen == null ? "" : examen.getId());
        c.add(examen == null ? "" : examen.getName());
        c.add(examen == null ? "" : examen.getCategory());
        c.add(examen == null ? "" : examen.getSampleType());
        c.add(s == null ? "" : fechaHora(s.getTakenAt()));
        c.add(s == null ? "" : s.getResponsible());
        c.add(s == null ? "" : s.getState().label());
        for (VicunaState estado : VicunaState.values()) {
            c.add(s == null ? "" : traza(s, estado));
        }
        if (row != null) {
            c.add(row.name());
            c.add(row.value());
            c.add(row.unit());
            c.add(row.reference());
            c.add(row.flag());
            c.add("—".equals(row.observation()) ? "" : row.observation());
        } else if (s != null && s.getState() != VicunaState.PUBLICADA) {
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
        c.add(s == null || s.getState() != VicunaState.PUBLICADA ? "" : s.getNotes());
        return c;
    }

    private String traza(Vicuna s, VicunaState estado) {
        return s.getTraces().stream().filter(t -> t.getState() == estado).findFirst()
                .map(t -> fechaHora(t.getAt()) + " · " + t.getUserName())
                .orElse("");
    }

    private static String fecha(LocalDate d) {
        return d == null ? "" : d.format(FECHA);
    }

    private static String fechaHora(LocalDateTime d) {
        return d == null ? "" : d.format(FECHA_HORA);
    }

    private static void linea(StringBuilder out, List<String> campos) {
        for (int i = 0; i < campos.size(); i++) {
            if (i > 0) {
                out.append(',');
            }
            out.append(campo(campos.get(i)));
        }
        out.append("\r\n");
    }

    static String campo(String valor) {
        String v = valor == null ? "" : valor;
        if (!v.isEmpty() && "=+@\t\r".indexOf(v.charAt(0)) >= 0
                || v.startsWith("-") && !v.matches("-\\d+([.,]\\d+)?")) {
            v = "'" + v;
        }
        return "\"" + v.replace("\"", "\"\"") + "\"";
    }
}
