package pe.vizcacha.laboratorio;

import javafx.application.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.awt.GraphicsEnvironment;

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
