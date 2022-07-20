package com.klimuts.snxgui.controller;

import com.klimuts.snxgui.di.annotation.Component;
import com.klimuts.snxgui.exception.ShownOnModalException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

import java.io.IOException;

@Component
public class PasswordWindowController extends WindowController {

    @FXML public ToggleButton unmaskButton;
    @FXML public PasswordField passwordField;
    @FXML public TextField passwordTextField;

    public void initialize() {
        this.onUnmaskButtonClick(null);
    }

    @FXML
    public void onUnmaskButtonClick(ActionEvent actionEvent) {
        if (unmaskButton.isSelected()) {
            unmaskButton.getStyleClass().remove("unmask");
            unmaskButton.getStyleClass().add("mask");

            passwordTextField.setText(passwordField.getText());
            passwordTextField.setVisible(true);
            passwordField.setVisible(false);
            return;
        }
        unmaskButton.getStyleClass().remove("mask");
        unmaskButton.getStyleClass().add("unmask");

        passwordField.setText(passwordTextField.getText());
        passwordField.setVisible(true);
        passwordTextField.setVisible(false);
    }

    @FXML
    public void onOkButtonClick(ActionEvent actionEvent) {
        String passcode = getPasswordValue();
        connectionService.connect(passcode);
        try {
            String error = connectionService.checkConnectionError();
            if (!connectionService.isConnected() && !error.isEmpty()) {
                throw new ShownOnModalException(error);
            }
        } catch (IOException e) {
            throw new ShownOnModalException("SNX Client: Cannot read connection state file");
        }
        modalWindowService.closeModalWindow();
    }

    private String getPasswordValue() {
        return unmaskButton.isSelected() ? passwordTextField.getText() : passwordField.getText();
    }

}
