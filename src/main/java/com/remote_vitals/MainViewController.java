package com.remote_vitals;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.stereotype.Controller;

@Controller
public class MainViewController {

    @FXML
    public void handleLogin(ActionEvent event) {
        System.out.println("Login button clicked");
        // Add your login logic here
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }
}