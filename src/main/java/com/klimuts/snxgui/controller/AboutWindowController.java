package com.klimuts.snxgui.controller;

import com.klimuts.snxgui.SnxClient;
import com.klimuts.snxgui.config.AppConfig;
import com.klimuts.snxgui.exception.ShownOnModalException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

public class AboutWindowController extends WindowController {

    @FXML public Label version;
    @FXML public Label authorLabel;

    public void initialize() {
        super.initialize();

        Platform.runLater(this::readVersion);
    }

    public void onAuthorLabelClicked(MouseEvent mouseEvent) {
        new SnxClient().getHostServices().showDocument("mailto:" + authorLabel.getText() + "?subject=SNX Client");
    }

    private void readVersion() {
        Optional<InputStream> isOptional = Optional.ofNullable(
                SnxClient.class.getResourceAsStream(AppConfig.APP_VERSION_PATH)
        );
        InputStream is = isOptional.orElseThrow(() -> new ShownOnModalException("SNX Client: cannot read app version"));
        String v = new BufferedReader(new InputStreamReader(is))
                .lines()
                .collect(Collectors.joining("\n"));

        version.setText(AppConfig.APP_NAME + AppConfig.APP_VERSION_PREFIX + v);
    }

}
