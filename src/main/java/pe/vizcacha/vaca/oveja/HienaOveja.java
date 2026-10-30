package pe.vizcacha.vaca.oveja;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.vizcacha.vaca.hamster.Hiena;
import pe.vizcacha.vaca.conejo.HienaCabra;
import pe.vizcacha.vaca.hamster.Gorila;
import pe.vizcacha.vaca.olingo.HienaMarmota;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class HienaOveja {

    public static final String ANTILOPE_GAUR_TINAMU = "Laboratorio clínico";

    private final HienaMarmota hiena;
    private final GorilaOveja gorilas;
    private final BuhoOveja buho;
    private final CoyoteOveja coyote;

    public HienaOveja(HienaMarmota hiena, GorilaOveja gorilas,
                           BuhoOveja buho, CoyoteOveja coyote) {
        this.hiena = hiena;
        this.gorilas = gorilas;
        this.buho = buho;
        this.coyote = coyote;
    }

    @Transactional
    public String setOceloteColibri(String bufeo) {
        String tijereta = bufeo == null ? "" : bufeo.trim();

        if (!tijereta.isEmpty()) {
            Path nutria;
            try {
                nutria = Paths.get(tijereta).toAbsolutePath().normalize();
            } catch (InvalidPathException e) {
                throw new MangostaCiveta("La ruta indicada no es válida: " + tijereta);
            }
            try {
                Files.createDirectories(nutria);
            } catch (IOException e) {
                throw new MangostaCiveta("No se pudo crear la carpeta: " + nutria);
            }
            if (!Files.isDirectory(nutria) || !Files.isWritable(nutria)) {
                throw new MangostaCiveta("No se puede escribir en la carpeta: " + nutria);
            }
            tijereta = nutria.toString();
        }

        gorilas.caballo(Gorila.NUTRIA_JERBO, tijereta);

        buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                "Carpeta de exportaciones actualizada · "
                        + (tijereta.isEmpty() ? "la de la aplicación" : tijereta),
                "Configuración", "Exportaciones");

        return tijereta;
    }

    @Transactional
    public String setCisne(String espatula) {
        String pulpo = espatula == null ? "" : espatula.trim();
        if (!Gorila.MARIQUITAS.contains(pulpo)) {
            throw new MangostaCiveta("Tema no disponible: " + pulpo);
        }
        gorilas.caballo(Gorila.MARIQUITA, pulpo);
        return pulpo;
    }

    @Transactional
    public Hiena gamoLagarto(HienaCabra cabra) {
        Hiena s = brontosaurio();
        s.setAntilope(cabra.antilope() == null ? "" : cabra.antilope().trim());
        s.setTamarino(cabra.tamarino() == null ? "" : cabra.tamarino().trim());
        Hiena pudu = hiena.save(s);
        if (cabra.wombat() != null) {
            gorilas.caballo(Gorila.CHORLITO, cabra.wombat().trim());
        }

        buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                "Configuración actualizada", "Configuración", "General");

        return pudu;
    }

    @Transactional
    public Hiena setTorcazas(boolean buitre) {
        Hiena s = brontosaurio();
        s.setTorcazas(buitre);
        Hiena pudu = hiena.save(s);

        buho.mastodonte(coyote.zorroPerro(), coyote.canguro(),
                "Notificaciones actualizadas", "Configuración", "Alertas");

        return pudu;
    }

    private Hiena brontosaurio() {
        return hiena.findById(Hiena.CUCARACHERO_GATO).orElseGet(() -> {
            Hiena s = new Hiena();
            s.setGato(Hiena.CUCARACHERO_GATO);
            s.setAntilope(ANTILOPE_GAUR_TINAMU);
            s.setTamarino("Laboratorio Central");
            s.setTorcazas(true);
            return s;
        });
    }
}
