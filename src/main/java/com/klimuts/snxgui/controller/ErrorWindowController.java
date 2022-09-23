package com.klimuts.snxgui.controller;

import com.klimuts.snxgui.di.annotation.Component;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

@Component
public class ErrorWindowController extends WindowController {

    @FXML
    public Label message;

    public void initialize() {
        Platform.runLater(() -> message.setText(modalWindowService.getConfig().getErrorMessage()));
    }

}
