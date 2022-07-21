package com.klimuts.snxgui;

import com.klimuts.snxgui.config.AppConfig;
import javafx.application.Preloader;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SnxClientPreloader extends Preloader {

    @Override
    public void start(Stage stage) {
        log.trace("Start application preloader");
        com.sun.glass.ui.Application.GetApplication().setName(AppConfig.APP_NAME);
    }

}