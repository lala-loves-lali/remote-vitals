package com.remote_vitals;

import com.remote_vitals.frontend.utils.ScreenPaths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

/**
 * JavaFX Application entry point
 * This class handles the initialization and startup of the JavaFX application
 */
public class JavaFXApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        context = org.springframework.boot.SpringApplication.run(RemoteVitalsApplication.class);
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        // You can switch between these options for testing:
        
        // Option 1: Start with login screen (for production)
        // loadScreen(stage, ScreenPaths.LOGIN_PAGE, ScreenPaths.TITLE_LOGIN);
        
        // Option 2: Start with dashboard selector (for testing)
        loadScreen(stage, ScreenPaths.DASHBOARD_SELECTOR, ScreenPaths.TITLE_DASHBOARD_SELECTOR);
    }
    
    /**
     * Helper method to load a screen
     * 
     * @param stage The stage to load the screen into
     * @param screenPath The path to the FXML file
     * @param title The title for the window
     * @throws IOException If the FXML file cannot be loaded
     */
    private void loadScreen(Stage stage, String screenPath, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JavaFXApplication.class.getResource(screenPath));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(title);
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