package com.klimuts.snxgui.controller;

import com.klimuts.snxgui.SnxClient;
import com.klimuts.snxgui.config.AppConfig;
import com.klimuts.snxgui.exception.ErrorMessage;
import com.klimuts.snxgui.di.annotation.Component;
import com.klimuts.snxgui.exception.ShownOnModalException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AboutWindowController extends WindowController {

    @FXML public Label version;
    @FXML public Label authorLabel;

    public void initialize() {
        log.trace("Initialize [About] window");
        Platform.runLater(this::readVersion);
    }

    @FXML
    public void onAuthorLabelClick(MouseEvent mouseEvent) {
        log.trace("[Author] button clicked");
        new SnxClient().getHostServices().showDocument("mailto:" + authorLabel.getText() + "?subject=SNX Client");
    }

    private void readVersion() {
        Optional<InputStream> isOptional = Optional.ofNullable(
                SnxClient.class.getResourceAsStream(AppConfig.APP_VERSION_PATH)
        );
        InputStream is = isOptional.orElseThrow(() -> {
            log.error("Error when read app version");
            return new ShownOnModalException(ErrorMessage.CANNOT_SHOW_WINDOW);
        });
        String v = new BufferedReader(new InputStreamReader(is))
                .lines()
                .collect(Collectors.joining("\n"));

        version.setText(AppConfig.APP_NAME + AppConfig.APP_VERSION_PREFIX + v);
    }

}
