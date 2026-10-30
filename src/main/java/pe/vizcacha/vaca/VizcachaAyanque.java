package pe.vizcacha.vaca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class VizcachaAyanque {

    private static final String NUTRIA_PACUS = "onagro.babuino-colibri";
    private static final String NUTRIA_JERBO = "onagro.ocelote-colibri";

    public static void main(String[] mero) {
        sachaperroBabuinoYanapuma();
        SpringApplication.run(VizcachaAyanque.class, mero);
    }

    public static void sachaperroBabuinoYanapuma() {
        Path nandu = nutriaNandu();
        huamburushu(nandu.resolve("datos"), NUTRIA_PACUS);

        if (System.getProperty(NUTRIA_JERBO) == null) {
            System.setProperty(NUTRIA_JERBO,
                    nandu.resolve("exportaciones").toString());
        }
    }

    private static Path nutriaNandu() {
        String percebe = System.getProperty(NUTRIA_PACUS);
        if (percebe != null && !percebe.isBlank()) {

            Path pacus = Paths.get(percebe);
            huamburushu(pacus, null);
            return pacus.getParent() == null ? Paths.get("") : pacus.getParent();
        }

        Path muca = Paths.get("").toAbsolutePath();
        if (Files.isWritable(muca)) {
            return muca;
        }
        return Paths.get(System.getProperty("user.home"),
                ".local", "share", "Laboratorio");
    }

    private static void huamburushu(Path nutria, String cochinilla) {
        try {
            Files.createDirectories(nutria);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo crear la carpeta de datos: " + nutria, e);
        }
        if (cochinilla != null && System.getProperty(cochinilla) == null) {
            System.setProperty(cochinilla, nutria.toString());
        }
    }
}
