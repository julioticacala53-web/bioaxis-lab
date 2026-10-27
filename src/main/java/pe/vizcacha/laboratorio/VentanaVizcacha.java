package pe.vizcacha.laboratorio;

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

public class VentanaVizcacha extends Application {

    private static final Logger log = LoggerFactory.getLogger(VentanaVizcacha.class);

    private static final String FONDO = "#bfc5c7";
    private static final String TURQUESA = "#273335";
    private static final String TENUE = "#425155";

    private ConfigurableApplicationContext spring;
    private Label mensaje;
    private StackPane raiz;
    private Stage ventana;
    private static volatile Stage principal;

    public static Stage principal() {
        return principal;
    }
    private VBox carga;

    @Override
    public void start(Stage ventana) {

        System.setProperty("laboratorio.ventana", "true");
        this.ventana = ventana;
        principal = ventana;
        ventana.setTitle("Laboratorio clínico — Sistema de Gestión de Laboratorio Clínico");
        ventana.setMinWidth(1100);
        ventana.setMinHeight(700);

        carga = pantallaDeCarga();

        carga.setStyle("-fx-background-color: " + FONDO + ";");

        raiz = new StackPane(carga);
        raiz.setStyle("-fx-background-color: " + FONDO + ";");
        ventana.setScene(new Scene(raiz, 1440, 900, Color.web(FONDO)));
        ventana.setMaximized(true);
        ventana.show();

        Thread arranque = new Thread(this::levantarServicio, "arranque-laboratorio");
        arranque.setDaemon(true);
        arranque.start();
    }

    private VBox pantallaDeCarga() {
        Label marca = new Label("Laboratorio clínico");
        marca.setFont(Font.font("System", FontWeight.BOLD, 28));
        marca.setTextFill(Color.web(TURQUESA));

        Label sub = new Label("SISTEMA DE GESTIÓN DE LABORATORIO CLÍNICO");
        sub.setFont(Font.font("System", 11));
        sub.setTextFill(Color.web(TENUE));

        mensaje = new Label("Iniciando el laboratorio...");
        mensaje.setFont(Font.font("System", 12));
        mensaje.setTextFill(Color.web(TENUE));
        VBox.setMargin(mensaje, new Insets(26, 0, 0, 0));

        VBox caja = new VBox(8, marca, sub, mensaje);
        caja.setAlignment(Pos.CENTER);
        return caja;
    }

    private void levantarServicio() {
        try {

            spring = new SpringApplicationBuilder(VizcachaApplication.class)
                    .headless(false)
                    .logStartupInfo(false)
                    .run(getParameters().getRaw().toArray(new String[0]));

            int puerto = spring.getEnvironment()
                    .getProperty("local.server.port", Integer.class, 0);

            log.info("Servicio en pie en el puerto {}", puerto);
            Platform.runLater(() -> mostrarInterfaz(puerto));
        } catch (Exception e) {
            Platform.runLater(() -> mensaje.setText(
                    "No se pudo iniciar el laboratorio: " + e.getMessage()));
        }
    }

    private void mostrarInterfaz(int puerto) {
        WebView navegador = new WebView();
        navegador.setContextMenuEnabled(false);
        navegador.setPageFill(Color.web(FONDO));

        raiz.getChildren().add(0, navegador);

        var motor = navegador.getEngine();
        motor.getLoadWorker().stateProperty().addListener((obs, antes, ahora) -> {

            log.info("Carga de la interfaz: {} -> {}", antes, ahora);
            if (ahora == Worker.State.FAILED || ahora == Worker.State.CANCELLED) {
                log.error("La carga fallo", motor.getLoadWorker().getException());
            }
            if (ahora == Worker.State.SUCCEEDED) {

                raiz.getChildren().remove(carga);
            } else if (ahora == Worker.State.FAILED) {
                mensaje.setText("No se pudo cargar la interfaz del laboratorio.");
            }
        });

        String url = "http://localhost:" + puerto + "/";
        log.info("Pidiendo la interfaz a {}", url);
        mensaje.setText("Cargando la interfaz...");
        motor.load(url);
    }

    @Override
    public void stop() {
        if (spring != null) {
            spring.close();
        }
        Platform.exit();
    }
}
