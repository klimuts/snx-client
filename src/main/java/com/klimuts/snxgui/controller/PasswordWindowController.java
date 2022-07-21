package com.klimuts.snxgui.controller;

import com.klimuts.snxgui.di.annotation.Component;
import com.klimuts.snxgui.exception.ErrorMessage;
import com.klimuts.snxgui.exception.ShownOnModalException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
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
        log.trace("[Unmask] button clicked");
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
        log.trace("[OK] button clicked");
        String passcode = getPasswordValue();
        connectionService.connect(passcode);
        try {
            String error = connectionService.checkConnectionError();
            if (!connectionService.isConnected() && !error.isEmpty()) {
                log.error("SNX connection error: {}", error);
                throw new ShownOnModalException(error);
            }
        } catch (IOException e) {
            log.error("Connection error, cannot read the connection state file", e);
            throw new ShownOnModalException(ErrorMessage.CANNOT_PERFORM_ACTION);
        }
        modalWindowService.closeModalWindow();
    }

    private String getPasswordValue() {
        return unmaskButton.isSelected() ? passwordTextField.getText() : passwordField.getText();
    }

}
