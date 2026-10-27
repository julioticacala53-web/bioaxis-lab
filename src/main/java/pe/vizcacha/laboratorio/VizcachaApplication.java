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

    /**
     * Decide donde viven los datos y las exportaciones, y crea las carpetas.
     *
     * SQLite crea el archivo de la base de datos pero no la carpeta que lo
     * contiene, asi que tiene que existir antes de que el pool abra la
     * primera conexion.
     *
     * Dos modos, resueltos solos:
     *
     *  - PORTATIL, si se puede escribir en el directorio de trabajo. Es el
     *    caso del .exe en el USB: launch4j arranca con chdir a su carpeta, y
     *    los datos quedan ahi y viajan con el pendrive.
     *  - INSTALADO, si no se puede. Es el caso de la aplicacion de Linux en
     *    /opt: los datos van al perfil del usuario, que es donde deben estar
     *    cuando el programa es del sistema y no del usuario.
     *
     * Con -Dapp.data-dir=RUTA se fuerza cualquiera de los dos.
     */
    public static void ensureDataDirectory() {
        Path base = carpetaBase();
        crear(base.resolve("datos"), CARPETA_DATOS);
        // Las exportaciones se crean a demanda; aqui solo se fija la ruta.
        if (System.getProperty(CARPETA_EXPORTACIONES) == null) {
            System.setProperty(CARPETA_EXPORTACIONES,
                    base.resolve("exportaciones").toString());
        }
    }

    private static Path carpetaBase() {
        String configurada = System.getProperty(CARPETA_DATOS);
        if (configurada != null && !configurada.isBlank()) {
            // Ya viene impuesta: se respeta tal cual, carpeta de datos incluida.
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
