package com.klimuts.snxgui.controller;

import com.klimuts.snxgui.di.annotation.Autowired;
import com.klimuts.snxgui.service.ConfigService;
import com.klimuts.snxgui.service.ConnectionService;
import com.klimuts.snxgui.service.ModalWindowService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class WindowController {

    @Autowired protected ConfigService configService;
    @Autowired protected ConnectionService connectionService;
    @Autowired protected ModalWindowService modalWindowService;

    @FXML
    protected void onCloseButtonClick(ActionEvent actionEvent) {
        modalWindowService.closeModalWindow();
    }

}
