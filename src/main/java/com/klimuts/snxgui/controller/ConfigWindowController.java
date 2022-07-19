package com.klimuts.snxgui.controller;

import com.klimuts.snxgui.di.Context;
import com.klimuts.snxgui.exception.ShownOnModalException;
import com.klimuts.snxgui.model.ConfigKey;
import com.klimuts.snxgui.service.ConfigService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Map;

public class ConfigWindowController extends WindowController {

    @FXML public TextField serverAddress;
    @FXML public TextField login;
    @FXML public CheckBox enableDebug;
    @FXML public CheckBox disconnectOnExit;

    private ConfigService configService;

    public void initialize() {
        super.initialize();
        configService = Context.getBean(ConfigService.class);

        Map<ConfigKey, String> config = configService.getConfig();

        serverAddress.setText(config.get(ConfigKey.SERVER_ADDRESS));
        login.setText(config.get(ConfigKey.LOGIN));
        enableDebug.setSelected("yes".equalsIgnoreCase(config.get(ConfigKey.ENABLE_DEBUG)));
        disconnectOnExit.setSelected(Boolean.parseBoolean(config.get(ConfigKey.DISCONNECT_ON_EXIT)));
    }

    @FXML
    public void onSaveButtonClick(ActionEvent actionEvent) {
        Map<ConfigKey, String> config = configService.getConfig();

        config.put(ConfigKey.SERVER_ADDRESS, serverAddress.getText());
        config.put(ConfigKey.LOGIN, login.getText());
        config.put(ConfigKey.ENABLE_DEBUG, enableDebug.isSelected() ? "yes" : "no");
        config.put(ConfigKey.DISCONNECT_ON_EXIT, disconnectOnExit.isSelected() ? "true" : "false");

        try {
            configService.saveConfig(config);
        } catch (IOException e) {
            throw new ShownOnModalException("SNX Client: cannot save config files");
        }
        modalWindowService.closeModalWindow();
    }

}
