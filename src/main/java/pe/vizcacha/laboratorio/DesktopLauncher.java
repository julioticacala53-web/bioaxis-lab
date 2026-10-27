package pe.vizcacha.laboratorio;

import javafx.application.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.awt.GraphicsEnvironment;

/**
 * Punto de entrada de la aplicacion de escritorio.
 *
 * NO extiende Application a proposito. Cuando la clase principal de la JVM
 * extiende javafx.application.Application y JavaFX no esta en el
 * module-path, el lanzador aborta con "faltan los componentes de JavaFX
 * runtime". Delegando en VentanaVizcacha esa comprobacion no se dispara y
 * JavaFX funciona desde el classpath, que es como viaja dentro del jar.
 *
 * Con -Dlaboratorio.sin-ventana=true, o en un equipo sin pantalla, se queda como
 * servicio web: asi se ejecuta en desarrollo y en las pruebas.
 */
public final class DesktopLauncher {

    private static final String SIN_VENTANA = "laboratorio.sin-ventana";

    private DesktopLauncher() {
    }

    public static void main(String[] args) {
        VizcachaApplication.ensureDataDirectory();

        if (Boolean.getBoolean(SIN_VENTANA) || GraphicsEnvironment.isHeadless()) {
            new SpringApplicationBuilder(VizcachaApplication.class).run(args);
            return;
        }
        Application.launch(VentanaVizcacha.class, args);
    }
}
