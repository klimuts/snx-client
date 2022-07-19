package com.klimuts.snxgui;

import com.klimuts.snxgui.config.AppConfig;
import javafx.application.Preloader;
import javafx.stage.Stage;

public class SnxClientPreloader extends Preloader {

    @Override
    public void start(Stage stage) {
        com.sun.glass.ui.Application.GetApplication().setName(AppConfig.APP_NAME);
    }

}