package com.klimuts.snxgui;

import com.klimuts.snxgui.config.AppConfig;
import com.klimuts.snxgui.di.Context;
import com.klimuts.snxgui.di.ContextInitializer;
import com.klimuts.snxgui.di.annotation.Autowired;
import com.klimuts.snxgui.di.annotation.Component;
import com.klimuts.snxgui.model.enums.ConfigKey;
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
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Component
public class SnxClient extends Application {

    @Autowired private ConfigService configService;
    @Autowired private ConnectionService connectionService;
    @Autowired private ModalWindowService modalWindowService;

    private double xOffset = 0;
    private double yOffset = 0;

    private SystemTray systemTray;

    @Override
    public void start(Stage stage) {
        log.info("Starting SNX Client initialization...");
        try {
            Platform.setImplicitExit(false);

            Parent root = initMainStage(stage);
            initGlobalExceptionHandlers(stage);
            initDragSettings(stage, root);
            initSystemTray(stage);

            stage.show();
        } catch (Exception e) {
            log.error("Error when SNX Client initialization", e);
        }
        log.info("SNX Client successfully initialized!");
    }

    private Parent initMainStage(Stage stage) throws IOException {
        log.trace("Starting main window initialization");
        Context context = ContextInitializer.init(this, SnxClient.class.getPackageName());

        FXMLLoader loader = new FXMLLoader(SnxClient.class.getResource("main.fxml"));
        loader.setControllerFactory(context::getBean);
        Parent root = loader.load();

        Scene scene = new Scene(root, 640, 460);
        scene.setFill(Color.TRANSPARENT);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.getIcons().add(new javafx.scene.image.Image("app_icon.png"));
        stage.setTitle(AppConfig.APP_NAME);
        stage.setScene(scene);

        log.trace("Main window successfully initialized!");
        return root;
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
            log.error("Error when init system tray, system tray is null");
            throw new RuntimeException("Unable to load SystemTray!");
        }
        systemTray.installShutdownHook();

        systemTray.getMenu().add(new MenuItem(AppConfig.TRAY_ITEM_SHOW, e -> {
            Platform.runLater(() -> {
                log.trace("Tray [Show] menu item clicked");
                if (stage.isShowing()) {
                    stage.requestFocus();
                } else {
                    stage.show();
                }
            });
        })).setShortcut(AppConfig.TRAY_ITEM_SHOW_SHORTCUT);

        systemTray.getMenu().add(new MenuItem(AppConfig.TRAY_ITEM_QUIT, event -> {
            log.trace("Tray [Quit] menu item clicked");
            systemTray.shutdown();
            try {
                if (Boolean.parseBoolean(configService.getConfig().get(ConfigKey.DISCONNECT_ON_EXIT))) {
                    log.trace("Disconnect by [{}] config option", ConfigKey.DISCONNECT_ON_EXIT.name());
                    connectionService.disconnect();
                }
            } catch (Exception e) {
                log.error("Error when terminate application", e);
                Platform.exit();
            }
            log.trace("Terminate application");
            Platform.exit();
        })).setShortcut(AppConfig.TRAY_ITEM_QUIT_SHORTCUT);
    }

    private void initGlobalExceptionHandlers(Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
            Throwable rootCause = ExceptionUtils.findRootCause(e);
            log.trace("DefaultUncaughtExceptionHandler: " + rootCause.getMessage());
            Platform.runLater(() -> modalWindowService.showErrorWindow(stage, rootCause.getMessage()));
        });
        Thread.currentThread().setUncaughtExceptionHandler((thread, e) -> {
            Throwable rootCause = ExceptionUtils.findRootCause(e);
            log.trace("CurrentThreadUncaughtExceptionHandler: " + rootCause.getMessage());
            modalWindowService.showErrorWindow(stage, rootCause.getMessage());
        });
    }

    public static void main(String[] args) {
        log.trace("Start application main method");
        com.sun.javafx.application.LauncherImpl.launchApplication(SnxClient.class, SnxClientPreloader.class, args);
    }

}