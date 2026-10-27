package pe.vizcacha.laboratorio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class VizcachaApplication {

    private static final String CARPETA_DATOS = "app.data-dir";
    private static final String CARPETA_EXPORTACIONES = "app.export-dir";

    public static void main(String[] args) {
        ensureDataDirectory();
        SpringApplication.run(VizcachaApplication.class, args);
    }

    public static void ensureDataDirectory() {
        Path base = carpetaBase();
        crear(base.resolve("datos"), CARPETA_DATOS);

        if (System.getProperty(CARPETA_EXPORTACIONES) == null) {
            System.setProperty(CARPETA_EXPORTACIONES,
                    base.resolve("exportaciones").toString());
        }
    }

    private static Path carpetaBase() {
        String configurada = System.getProperty(CARPETA_DATOS);
        if (configurada != null && !configurada.isBlank()) {

            Path datos = Paths.get(configurada);
            crear(datos, null);
            return datos.getParent() == null ? Paths.get("") : datos.getParent();
        }

        Path trabajo = Paths.get("").toAbsolutePath();
        if (Files.isWritable(trabajo)) {
            return trabajo;
        }
        return Paths.get(System.getProperty("user.home"),
                ".local", "share", "Laboratorio");
    }

    private static void crear(Path carpeta, String propiedad) {
        try {
            Files.createDirectories(carpeta);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo crear la carpeta de datos: " + carpeta, e);
        }
        if (propiedad != null && System.getProperty(propiedad) == null) {
            System.setProperty(propiedad, carpeta.toString());
        }
    }
}
