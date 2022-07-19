package com.klimuts.snxgui;

import com.klimuts.snxgui.config.AppConfig;
import com.klimuts.snxgui.di.Context;
import com.klimuts.snxgui.model.ConfigKey;
import com.klimuts.snxgui.service.ConfigService;
import com.klimuts.snxgui.service.ConnectionService;
import com.klimuts.snxgui.service.ModalWindowService;
import com.klimuts.snxgui.util.ExceptionUtils;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SnxClient extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    private SystemTray systemTray;
    private ConfigService configService;
    private ConnectionService connectionService;
    private ModalWindowService modalWindowService;

    @Override
    public void start(Stage stage) throws IOException {
        Platform.setImplicitExit(false);

        initDependencies();
        initGlobalExceptionHandlers(stage);

        FXMLLoader loader = new FXMLLoader(SnxClient.class.getResource("main.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 640, 460);
        scene.setFill(Color.TRANSPARENT);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.getIcons().add(new javafx.scene.image.Image("app_icon.png"));
        stage.setTitle(AppConfig.APP_NAME);
        stage.setScene(scene);
        stage.show();

        initDragSettings(stage, root);
        initSystemTray(stage);
    }

    private void initDependencies() throws IOException {
        Context.init();
        modalWindowService = Context.getBean(ModalWindowService.class);
        connectionService = Context.getBean(ConnectionService.class);
        configService = Context.getBean(ConfigService.class);
        configService.initConfig();
    }

    private void initDragSettings(Stage stage, Parent root) {
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public void initSystemTray(Stage stage) {
        SystemTray.DEBUG = false;
        this.systemTray = SystemTray.get("SNX");
        if (systemTray == null) {
            throw new RuntimeException("Unable to load SystemTray!");
        }
        systemTray.installShutdownHook();
        systemTray.getMenu().add(new MenuItem("Show", e -> {
            Platform.runLater(() -> {
                if (stage.isShowing()) {
                    stage.requestFocus();
                } else {
                    stage.show();
                }
            });
        })).setShortcut('o');

        systemTray.getMenu().add(new MenuItem("Quit", e -> {
            systemTray.shutdown();
            if (Boolean.parseBoolean(configService.getConfig().get(ConfigKey.DISCONNECT_ON_EXIT))) {
                connectionService.disconnect();
            }
            Platform.exit();
        })).setShortcut('q');
    }

    private void initGlobalExceptionHandlers(Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
            Throwable rootCause = ExceptionUtils.findRootCause(e);
            Platform.runLater(() -> modalWindowService.showErrorWindow(stage, rootCause.getMessage()));
        });
        Thread.currentThread().setUncaughtExceptionHandler((thread, e) -> {
            Throwable rootCause = ExceptionUtils.findRootCause(e);
            modalWindowService.showErrorWindow(stage, rootCause.getMessage());
        });
    }

    public static void main(String[] args) {
        com.sun.javafx.application.LauncherImpl.launchApplication(SnxClient.class, SnxClientPreloader.class, args);
    }

}