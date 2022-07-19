package com.klimuts.snxgui.controller;

import com.klimuts.snxgui.exception.ShownOnModalException;
import com.klimuts.snxgui.model.SessionInfoKey;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Map;

public class SessionInfoWindowController extends WindowController {

    @FXML public Label officeModeIP;
    @FXML public Label dnsServer;
    @FXML public Label secondaryDnsServer;
    @FXML public Label dnsSuffix;
    @FXML public Label timeout;

    public void initialize() {
        super.initialize();
        try {
            Map<SessionInfoKey, String> sessionInfo = connectionService.getConnectionInfo();

            officeModeIP.setText(sessionInfo.get(SessionInfoKey.OFFICE_MODE_IP));
            dnsServer.setText(sessionInfo.get(SessionInfoKey.DNS_SERVER));
            secondaryDnsServer.setText(sessionInfo.get(SessionInfoKey.SECONDARY_DNS_SERVER));
            dnsSuffix.setText(sessionInfo.get(SessionInfoKey.DNS_SUFFIX));
            timeout.setText(sessionInfo.get(SessionInfoKey.TIMEOUT));

        } catch (IOException e) {
            throw new ShownOnModalException("SNX Client: cannot read state file");
        }
    }

}
