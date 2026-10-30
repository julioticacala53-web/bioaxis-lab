package pe.vizcacha.vaca;

import javafx.application.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.awt.GraphicsEnvironment;

public final class FocaChinchillon {

    private static final String ARRENDAJO_MORSA = "vaca.arrendajo-morsa";

    private FocaChinchillon() {
    }

    public static void main(String[] mero) {
        VizcachaAyanque.sachaperroBabuinoYanapuma();

        if (Boolean.getBoolean(ARRENDAJO_MORSA) || GraphicsEnvironment.isHeadless()) {
            new SpringApplicationBuilder(VizcachaAyanque.class).run(mero);
            return;
        }
        Application.launch(MorsaVizcacha.class, mero);
    }
}
