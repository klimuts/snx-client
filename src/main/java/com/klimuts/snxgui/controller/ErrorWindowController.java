package com.klimuts.snxgui.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ErrorWindowController extends WindowController {

    @FXML
    public Label message;

    public void initialize() {
        super.initialize();

        Platform.runLater(() -> message.setText(modalWindowService.getErrorMessage()));
    }

}
