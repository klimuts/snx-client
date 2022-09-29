package com.klimuts.snxgui.controller;

import com.klimuts.snxgui.exception.ErrorMessage;
import com.klimuts.snxgui.di.annotation.Component;
import com.klimuts.snxgui.exception.ShownOnModalException;
import com.klimuts.snxgui.model.enums.ConfigKey;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class ConfigWindowController extends WindowController {

    @FXML public TextField serverAddress;
    @FXML public TextField login;
    @FXML public CheckBox enableDebug;
    @FXML public CheckBox disconnectOnExit;

    public void initialize() {
        log.trace("Initialize [Config] window");
        Map<ConfigKey, String> config = configService.getConfig();

        serverAddress.setText(config.get(ConfigKey.SERVER_ADDRESS));
        login.setText(config.get(ConfigKey.LOGIN));
        enableDebug.setSelected("yes".equalsIgnoreCase(config.get(ConfigKey.ENABLE_DEBUG)));
        disconnectOnExit.setSelected(Boolean.parseBoolean(config.get(ConfigKey.DISCONNECT_ON_EXIT)));
    }

    @FXML
    public void onSaveButtonClick(ActionEvent actionEvent) {
        log.trace("[Save] button clicked");
        Map<ConfigKey, String> config = configService.getConfig();

        config.put(ConfigKey.SERVER_ADDRESS, serverAddress.getText());
        config.put(ConfigKey.LOGIN, login.getText());
        config.put(ConfigKey.ENABLE_DEBUG, enableDebug.isSelected() ? "yes" : "no");
        config.put(ConfigKey.DISCONNECT_ON_EXIT, disconnectOnExit.isSelected() ? "true" : "false");

        try {
            configService.saveConfig(config);
        } catch (IOException e) {
            log.error("Error when save config files", e);
            throw new ShownOnModalException(ErrorMessage.CANNOT_PERFORM_ACTION);
        }
        modalWindowService.closeModalWindow();
    }

}
