package com.klimuts.snxgui.controller;

import com.klimuts.snxgui.di.annotation.Autowired;
import com.klimuts.snxgui.model.enums.WindowState;
import com.klimuts.snxgui.service.ConfigService;
import com.klimuts.snxgui.service.ConnectionService;
import com.klimuts.snxgui.service.ModalWindowService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WindowController {

    @Autowired protected ConfigService configService;
    @Autowired protected ConnectionService connectionService;
    @Autowired protected ModalWindowService modalWindowService;

    @FXML
    protected void onCloseButtonClick(ActionEvent actionEvent) {
        log.trace("[Close] button clicked");
        modalWindowService.closeModalWindow();
    }

    protected void toggleWindowState(WindowState state) {
        switch (state) {
            case CONNECTING -> getConnectingWindowState();
            case CONNECTED -> getConnectedWindowState();
            case DISCONNECTED -> getDisconnectedWindowState();
        }
    }

    protected void getConnectingWindowState() {
        // NOP, to override
    }

    protected void getConnectedWindowState() {
        // NOP, to override
    }

    protected void getDisconnectedWindowState() {
        // NOP, to override
    }

}
