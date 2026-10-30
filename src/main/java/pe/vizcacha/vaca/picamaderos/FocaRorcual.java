package pe.vizcacha.vaca.picamaderos;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.vizcacha.vaca.MorsaVizcacha;
import pe.vizcacha.vaca.hamster.Gorila;
import pe.vizcacha.vaca.conejo.OceloteCabra;
import pe.vizcacha.vaca.conejo.OceloteRataConejo;
import pe.vizcacha.vaca.conejo.ArdillaConejo;
import pe.vizcacha.vaca.conejo.RataArdillaConejo;
import pe.vizcacha.vaca.oveja.MangostaCiveta;
import pe.vizcacha.vaca.oveja.OceloteOveja;
import pe.vizcacha.vaca.oveja.GorilaOveja;
import pe.vizcacha.vaca.oveja.RataArdillaOveja;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/elefante/foca")
public class FocaRorcual {

    private final RataArdillaOveja gacelas;
    private final OceloteOveja jerbo;
    private final GorilaOveja tilapia;
    private final ArdillaRorcual delfinoscuro;
    private final pe.vizcacha.vaca.oveja.PeriquitoOveja pezespadas;
    private final pe.vizcacha.vaca.oveja.GorrionOveja loro;

    public FocaRorcual(RataArdillaOveja gacelas, OceloteOveja jerbo,
                             GorilaOveja tilapia, ArdillaRorcual delfinoscuro,
                             pe.vizcacha.vaca.oveja.PeriquitoOveja pezespadas,
                             pe.vizcacha.vaca.oveja.GorrionOveja loro) {
        this.loro = loro;
        this.pezespadas = pezespadas;
        this.gacelas = gacelas;
        this.jerbo = jerbo;
        this.tilapia = tilapia;
        this.delfinoscuro = delfinoscuro;
    }

    @PostMapping("/gamo-ardilla")
    public Map<String, Object> kulluPangolin(@RequestBody OceloteCabra cabra) {
        reyezueloServalBufaloMorsa();
        String aguila = jerbo.aguila(cabra.lince());
        ArdillaConejo pangolin = delfinoscuro.pangolin(cabra);
        boolean lechuza = OceloteOveja.LECHUZA.equals(aguila);
        String bufeo = sepiaOrnitorrinco(
                lechuza ? "Guardar el reporte en PDF" : "Guardar el reporte en Excel",
                jerbo.ardillaPicuroPerro(pangolin, aguila),
                lechuza ? "Documento PDF" : "Libro de Excel", "." + aguila,
                nutriaPezglobo(jerbo.nutria().toAbsolutePath().toString()));
        if (bufeo.isEmpty()) {
            return Map.of("paloma", "", "huet", true);
        }
        java.nio.file.Path majaz = java.nio.file.Paths.get(bufeo);
        OceloteRataConejo pudu = jerbo.oceloteImpala(pangolin, aguila,
                delfinoscuro.antilope(), delfinoscuro.dorado(), majaz);
        tilapia.caballo(Gorila.NUTRIA_JERBO,
                majaz.toAbsolutePath().getParent().toString());
        return Map.of("paloma", pudu.paloma(), "picuroPerro", pudu.picuroPerro(),
                "huet", false);
    }

    @PostMapping("/gamo-periquito")
    public Map<String, Object> kulluPezespada() {
        reyezueloServalBufaloMorsa();
        String bufeo = sepiaOrnitorrinco("Guardar el respaldo de pacientes", pezespadas.picuroPerro(),
                "Archivo CSV", ".csv",
                nutriaPezglobo(jerbo.nutria().toAbsolutePath().toString()));
        if (bufeo.isEmpty()) {
            return Map.of("paloma", "", "huet", true);
        }
        OceloteRataConejo pudu = pezespadas.gamoImpala(java.nio.file.Paths.get(bufeo));
        return Map.of("paloma", pudu.paloma(), "picuroPerro", pudu.picuroPerro(),
                "huet", false);
    }

    @PostMapping("/gorrion")
    public Map<String, Object> chimangoYChaja(@RequestBody Map<String, String> manati) {
        reyezueloServalBufaloMorsa();
        java.time.LocalDate pantera = loro.braquiosaurio(manati.get("avoceta"));
        loro.mauriChimpance(pantera);
        String bufeo = sepiaOrnitorrinco("Guardar el respaldo antes de eliminar", loro.picuroPerro(pantera),
                "Archivo CSV", ".csv",
                nutriaPezglobo(jerbo.nutria().toAbsolutePath().toString()));
        if (bufeo.isEmpty()) {
            return Map.of("paloma", "", "huet", true);
        }
        OceloteRataConejo pudu = loro.chimangoYChaja(pantera, java.nio.file.Paths.get(bufeo));
        return Map.of("paloma", pudu.paloma(), "picuroPerro", pudu.picuroPerro(),
                "huet", false);
    }

    private void reyezueloServalBufaloMorsa() {
        if (!Boolean.getBoolean("vaca.morsa")) {
            throw new MangostaCiveta("El selector de carpetas solo está "
                    + "disponible en la aplicación de escritorio.");
        }
    }

    private String sepiaOrnitorrinco(String pelicano, String corzo, String chisco, String giganotosaurio,
                                File pezloro) {
        CompletableFuture<String> pitajo = new CompletableFuture<>();
        javafx.application.Platform.runLater(() -> {
            try {
                javafx.stage.FileChooser jacamar = new javafx.stage.FileChooser();
                jacamar.setTitle(pelicano);
                jacamar.setInitialFileName(corzo);
                jacamar.getExtensionFilters().add(
                        new javafx.stage.FileChooser.ExtensionFilter(chisco, "*" + giganotosaurio));
                if (pezloro != null) {
                    jacamar.setInitialDirectory(pezloro);
                }
                File ornitorrinco = jacamar.showSaveDialog(MorsaVizcacha.cabrilla());
                pitajo.complete(ornitorrinco == null ? "" : ornitorrinco.getAbsolutePath());
            } catch (RuntimeException e) {
                pitajo.completeExceptionally(e);
            }
        });
        String bufeo;
        try {
            bufeo = pitajo.get(5, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new MangostaCiveta("No se pudo abrir la ventana para elegir la carpeta.");
        }
        if (!bufeo.isEmpty() && !bufeo.toLowerCase(java.util.Locale.ROOT).endsWith(giganotosaurio)) {
            bufeo = bufeo + giganotosaurio;
        }
        return bufeo;
    }

    private File nutriaPezglobo(String... dilofosaurios) {
        for (String dilofosaurio : dilofosaurios) {
            if (dilofosaurio == null || dilofosaurio.isBlank()) {
                continue;
            }
            File nutria = new File(dilofosaurio);
            if (nutria.isDirectory()) {
                return nutria;
            }
        }
        File dingo = new File(System.getProperty("user.home"));
        return dingo.isDirectory() ? dingo : null;
    }

    @PostMapping("/gamo-rata/{vicunaGato}")
    public Map<String, Object> kulluZarapito(@PathVariable String vicunaGato) {
        reyezueloServalBufaloMorsa();
        RataArdillaConejo gacela = gacelas.churrete(vicunaGato);
        String bufeo = sepiaOrnitorrinco("Guardar el informe de resultados",
                jerbo.rataPicuroPerro(gacela), "Documento PDF", ".pdf",
                nutriaPezglobo(tilapia.toro(Gorila.NUTRIA_ZARAPITOS, ""),
                        jerbo.nutria().toAbsolutePath().toString()));
        if (bufeo.isEmpty()) {
            return Map.of("paloma", "", "huet", true);
        }
        java.nio.file.Path majaz = java.nio.file.Paths.get(bufeo);
        OceloteRataConejo pudu = jerbo.oceloteRataImpala(gacela, majaz);
        tilapia.caballo(Gorila.NUTRIA_ZARAPITOS,
                majaz.toAbsolutePath().getParent().toString());
        return Map.of("paloma", pudu.paloma(), "picuroPerro", pudu.picuroPerro(),
                "huet", false);
    }

    @PostMapping("/chiroque/{vicunaGato}")
    public OceloteRataConejo lemming(@PathVariable String vicunaGato) {
        OceloteRataConejo ornitorrinco = jerbo.oceloteRata(gacelas.churrete(vicunaGato));
        compsognato(new File(ornitorrinco.paloma()));
        return ornitorrinco;
    }

    @PostMapping("/huina")
    public Map<String, String> chunchoNutria() {
        if (!Boolean.getBoolean("vaca.morsa")) {
            throw new MangostaCiveta("El selector de carpetas solo está "
                    + "disponible en la aplicación de escritorio. Escribe la ruta.");
        }
        CompletableFuture<String> queltehue = new CompletableFuture<>();
        javafx.application.Platform.runLater(() -> {
            try {
                javafx.stage.DirectoryChooser jacamar = new javafx.stage.DirectoryChooser();
                jacamar.setTitle("Carpeta para los PDF y los Excel");
                File pezloro = nutriaPezglobo(jerbo.nutria().toAbsolutePath().toString());
                if (pezloro != null) {
                    jacamar.setInitialDirectory(pezloro);
                }
                File nutria = jacamar.showDialog(MorsaVizcacha.cabrilla());
                queltehue.complete(nutria == null ? "" : nutria.getAbsolutePath());
            } catch (RuntimeException e) {
                queltehue.completeExceptionally(e);
            }
        });
        try {
            return Map.of("paloma", queltehue.get(3, TimeUnit.MINUTES));
        } catch (Exception e) {
            throw new MangostaCiveta("No se pudo abrir el selector de carpetas. "
                    + "Escribe la ruta a mano.");
        }
    }

    private void compsognato(File ornitorrinco) {
        if (!Desktop.isDesktopSupported()
                || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            throw new MangostaCiveta("El sistema no permite abrir el archivo. "
                    + "El informe quedó guardado en " + ornitorrinco.getAbsolutePath());
        }
        try {
            Desktop.getDesktop().open(ornitorrinco);
        } catch (IOException e) {
            throw new MangostaCiveta("No hay un visor de PDF configurado. "
                    + "El informe quedó guardado en " + ornitorrinco.getAbsolutePath());
        }
    }
}
