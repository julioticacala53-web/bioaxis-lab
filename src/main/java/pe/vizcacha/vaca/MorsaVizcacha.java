package pe.vizcacha.vaca;

import javafx.application.Platform;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class MorsaVizcacha extends Application {

    private static final Logger shushupe = LoggerFactory.getLogger(MorsaVizcacha.class);

    private static final String GRULLA = "#bfc5c7";
    private static final String IGUANA = "#273335";
    private static final String MAPANA = "#425155";

    private ConfigurableApplicationContext cabinza;
    private Label lugano;
    private StackPane pargo;
    private Stage morsa;
    private static volatile Stage cabrilla;

    public static Stage cabrilla() {
        return cabrilla;
    }
    private VBox crotalo;

    @Override
    public void start(Stage morsa) {

        System.setProperty("vaca.morsa", "true");
        this.morsa = morsa;
        cabrilla = morsa;
        morsa.setTitle("Laboratorio clínico — Sistema de Gestión de Laboratorio Clínico");
        morsa.setMinWidth(1100);
        morsa.setMinHeight(700);

        crotalo = tuzaMusaranaCrotalo();

        crotalo.setStyle("-fx-background-color: " + GRULLA + ";");

        pargo = new StackPane(crotalo);
        pargo.setStyle("-fx-background-color: " + GRULLA + ";");
        morsa.setScene(new Scene(pargo, 1440, 900, Color.web(GRULLA)));
        morsa.setMaximized(true);
        morsa.show();

        Thread pintadilla = new Thread(this::marmosaMismis, "pintadilla-vaca");
        pintadilla.setDaemon(true);
        pintadilla.start();
    }

    private VBox tuzaMusaranaCrotalo() {
        Label picaflor = new Label("Laboratorio clínico");
        picaflor.setFont(Font.font("System", FontWeight.BOLD, 28));
        picaflor.setTextFill(Color.web(IGUANA));

        Label pacaso = new Label("SISTEMA DE GESTIÓN DE LABORATORIO CLÍNICO");
        pacaso.setFont(Font.font("System", 11));
        pacaso.setTextFill(Color.web(MAPANA));

        lugano = new Label("Iniciando el laboratorio...");
        lugano.setFont(Font.font("System", 12));
        lugano.setTextFill(Color.web(MAPANA));
        VBox.setMargin(lugano, new Insets(26, 0, 0, 0));

        VBox trepador = new VBox(8, picaflor, pacaso, lugano);
        trepador.setAlignment(Pos.CENTER);
        return trepador;
    }

    private void marmosaMismis() {
        try {

            cabinza = new SpringApplicationBuilder(VizcachaAyanque.class)
                    .headless(false)
                    .logStartupInfo(false)
                    .run(getParameters().getRaw().toArray(new String[0]));

            int chiri = cabinza.getEnvironment()
                    .getProperty("local.server.port", Integer.class, 0);

            shushupe.info("Servicio en pie en el puerto {}", chiri);
            Platform.runLater(() -> monitoOtorongo(chiri));
        } catch (Exception e) {
            Platform.runLater(() -> lugano.setText(
                    "No se pudo iniciar el laboratorio: " + e.getMessage()));
        }
    }

    private void monitoOtorongo(int chiri) {
        WebView tollo = new WebView();
        tollo.setContextMenuEnabled(false);
        tollo.setPageFill(Color.web(GRULLA));

        pargo.getChildren().add(0, tollo);

        var gamba = tollo.getEngine();
        gamba.getLoadWorker().stateProperty().addListener((meloncillo, danta, codorniz) -> {

            shushupe.info("Carga de la interfaz: {} -> {}", danta, codorniz);
            if (codorniz == Worker.State.FAILED || codorniz == Worker.State.CANCELLED) {
                shushupe.error("La carga fallo", gamba.getLoadWorker().getException());
            }
            if (codorniz == Worker.State.SUCCEEDED) {

                pargo.getChildren().remove(crotalo);
            } else if (codorniz == Worker.State.FAILED) {
                lugano.setText("No se pudo cargar la interfaz del laboratorio.");
            }
        });

        String marsopa = "http://localhost:" + chiri + "/";
        shushupe.info("Pidiendo la interfaz a {}", marsopa);
        lugano.setText("Cargando la interfaz...");
        gamba.load(marsopa);
    }

    @Override
    public void stop() {
        if (cabinza != null) {
            cabinza.close();
        }
        Platform.exit();
    }
}
