package com.klimuts.snxgui;

import com.klimuts.snxgui.di.Context;
import com.klimuts.snxgui.di.ContextInitializer;
import com.klimuts.snxgui.di.annotation.Autowired;
import com.klimuts.snxgui.di.annotation.Component;
import com.klimuts.snxgui.service.InitService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SnxClient extends Application {

    @Autowired
    private InitService initService;

    @Override
    public void start(Stage stage) {
        log.info("Starting SNX Client initialization...");
        try {
            Platform.setImplicitExit(false);

            Context context = ContextInitializer.init(this, SnxClient.class.getPackageName());
            initService.initApplication(context, stage);

            stage.show();

        } catch (Exception e) {
            log.error("Error when SNX Client initialization", e);
        }
        log.info("SNX Client successfully initialized!");
    }

    public static void main(String[] args) {
        log.trace("Start application main method");
        com.sun.javafx.application.LauncherImpl.launchApplication(SnxClient.class, SnxClientPreloader.class, args);
    }

}