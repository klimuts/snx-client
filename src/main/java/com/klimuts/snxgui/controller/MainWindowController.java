package com.klimuts.snxgui.controller;

import com.klimuts.snxgui.SnxClient;
import com.klimuts.snxgui.di.annotation.Component;
import com.klimuts.snxgui.model.ModalWindowConfig;
import com.klimuts.snxgui.model.enums.ModalWindowType;
import dorkbox.systemTray.SystemTray;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;

@Slf4j
@Component
public class MainWindowController extends WindowController {

    @FXML public Button closeButton;
    @FXML public Button minButton;
    @FXML public Button connectButton;
    @FXML public Button aboutButton;
    @FXML public Label statusIcon;
    @FXML public Label statusText;
    @FXML public Label connectHint;
    @FXML public Label sessionInfoButton;
    @FXML public Button settingsButton;
    @FXML public VBox mainBox;

    private SystemTray systemTray;
    private URL connectedTrayIconUrl;
    private URL disconnectedTrayIconUrl;

    public void initialize() {
        systemTray = SystemTray.get("SNX");

        connectedTrayIconUrl = SnxClient.class.getResource("tray_icons/connected_icon.png");
        disconnectedTrayIconUrl = SnxClient.class.getResource("tray_icons/disconnected_icon.png");

        updateMainWindow();
    }

    @FXML
    public void onCloseButtonClick(ActionEvent event) {
        log.trace("[Close] button clicked");
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onMinimizeButtonClick(ActionEvent event) {
        log.trace("[Minimize] button clicked");
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void onConnectButtonClick() {
        log.trace("[(Dis)Connect] button clicked");
        if (connectionService.isConnected()) {
            connectionService.disconnect();
            updateMainWindow();
        } else {
            ModalWindowConfig config = ModalWindowConfig.builder()
                    .windowType(ModalWindowType.PASSWORD_MODAL_WINDOW)
                    .closeOnMaskClick(false)
                    .parentStage((Stage) mainBox.getScene().getWindow())
                    .closeCallback(this::updateMainWindow)
                    .build();
            modalWindowService.openModalWindow(config);
        }
    }

    @FXML
    public void onAboutButtonClick(ActionEvent actionEvent) {
        log.trace("[About] button clicked");
        ModalWindowConfig config = ModalWindowConfig.builder()
                .windowType(ModalWindowType.ABOUT_MODAL_WINDOW)
                .closeOnMaskClick(true)
                .parentStage((Stage) mainBox.getScene().getWindow())
                .build();
        modalWindowService.openModalWindow(config);
    }

    @FXML
    public void onSettingsButtonClick(ActionEvent actionEvent) {
        log.trace("[Settings] button clicked");
        ModalWindowConfig config = ModalWindowConfig.builder()
                .windowType(ModalWindowType.SETTINGS_MODAL_WINDOW)
                .closeOnMaskClick(false)
                .parentStage((Stage) mainBox.getScene().getWindow())
                .build();
        modalWindowService.openModalWindow(config);
    }

    @FXML
    public void onSessionInfoButtonClick(MouseEvent actionEvent) {
        log.trace("[Session info] button clicked");
        ModalWindowConfig config = ModalWindowConfig.builder()
                .windowType(ModalWindowType.SESSION_MODAL_WINDOW)
                .closeOnMaskClick(true)
                .parentStage((Stage) mainBox.getScene().getWindow())
                .build();
        modalWindowService.openModalWindow(config);
    }

    private Boolean updateMainWindow() {
        if (connectionService.isConnected()) {
            log.trace("toggle [connected] window state");
            toggleConnectedWindowState();
        } else {
            log.trace("toggle [disconnected] window state");
            toggleDisconnectedWindowState();
        }
        return true;
    }

    private void toggleDisconnectedWindowState() {
        connectButton.setText("CONNECT");

        statusIcon.getStyleClass().clear();
        statusIcon.getStyleClass().add("status-icon-disconnected");

        systemTray.setImage(disconnectedTrayIconUrl);
        systemTray.setStatus("Disconnected");

        statusText.setText("Disconnected");
        statusText.getStyleClass().clear();
        statusText.getStyleClass().add("status-text-disconnected");

        sessionInfoButton.setVisible(false);
        connectHint.setVisible(true);
    }

    private void toggleConnectedWindowState() {
        connectButton.setText("DISCONNECT");

        statusIcon.getStyleClass().clear();
        statusIcon.getStyleClass().add("status-icon-connected");

        systemTray.setImage(connectedTrayIconUrl);
        systemTray.setStatus("Connected");

        statusText.setText("Connected");
        statusText.getStyleClass().clear();
        statusText.getStyleClass().add("status-text-connected");

        sessionInfoButton.setVisible(true);
        connectHint.setVisible(false);
    }

}
