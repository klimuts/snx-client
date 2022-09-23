package com.klimuts.snxgui.controller;

import com.klimuts.snxgui.di.annotation.Component;
import com.klimuts.snxgui.exception.ErrorMessage;
import com.klimuts.snxgui.exception.ShownOnModalException;
import com.klimuts.snxgui.model.enums.SessionInfoKey;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class SessionInfoWindowController extends WindowController {

    @FXML public Label officeModeIP;
    @FXML public Label dnsServer;
    @FXML public Label secondaryDnsServer;
    @FXML public Label dnsSuffix;
    @FXML public Label timeout;

    public void initialize() {
        try {
            log.trace("Initialize session info");
            Map<SessionInfoKey, String> sessionInfo = connectionService.getConnectionInfo();

            if (sessionInfo == null) {
                log.info("Session info is empty, but status is CONNECTED." +
                        " Connection established without using the \"SNX client\" application.");
            }

            officeModeIP.setText(sessionInfo.get(SessionInfoKey.OFFICE_MODE_IP));
            dnsServer.setText(sessionInfo.get(SessionInfoKey.DNS_SERVER));
            secondaryDnsServer.setText(sessionInfo.get(SessionInfoKey.SECONDARY_DNS_SERVER));
            dnsSuffix.setText(sessionInfo.get(SessionInfoKey.DNS_SUFFIX));
            timeout.setText(sessionInfo.get(SessionInfoKey.TIMEOUT));

        } catch (IOException e) {
            log.error("Cannot read state file", e);
            throw new ShownOnModalException(ErrorMessage.CANNOT_SHOW_WINDOW);
        }
    }

}
