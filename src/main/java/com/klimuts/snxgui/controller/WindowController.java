package com.klimuts.snxgui.controller;

import com.klimuts.snxgui.di.Context;
import com.klimuts.snxgui.service.ConnectionService;
import com.klimuts.snxgui.service.ModalWindowService;
import javafx.event.ActionEvent;

public class WindowController {

    protected ModalWindowService modalWindowService;
    protected ConnectionService connectionService;

    public void initialize() {
        connectionService = Context.getBean(ConnectionService.class);
        modalWindowService = Context.getBean(ModalWindowService.class);
    }

    public void onCloseButtonClick(ActionEvent actionEvent) {
        modalWindowService.closeModalWindow();
    }

}
