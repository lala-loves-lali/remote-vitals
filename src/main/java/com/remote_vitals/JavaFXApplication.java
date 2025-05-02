package com.remote_vitals;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

/**
 * JavaFX Application entry point
 * This is a standalone JavaFX application for testing
 */
public class JavaFXApplication extends Application {


    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        context = org.springframework.boot.SpringApplication.run(RemoteVitalsApplication.class);
    }
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFXApplication.class.getResource("/simple.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        // Configure the stage
        stage.setTitle("JavaFX Test Application");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method to launch the application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}