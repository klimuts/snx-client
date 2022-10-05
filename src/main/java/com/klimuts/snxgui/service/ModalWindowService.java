package com.klimuts.snxgui.service;

import com.klimuts.snxgui.exception.ErrorMessage;
import com.klimuts.snxgui.di.annotation.Autowired;
import com.klimuts.snxgui.di.annotation.Component;
import com.klimuts.snxgui.exception.ShownOnModalException;
import com.klimuts.snxgui.model.ModalWindowConfig;
import com.klimuts.snxgui.model.enums.ModalWindowType;
import com.klimuts.snxgui.util.TaskUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
@Component
public class ModalWindowService {

    @Autowired ModalPaneLoader modalsProvider;

    @Getter
    private ModalWindowConfig config;
    private Parent parentRoot;
    private Stage modalStage;

    public void openModalWindow(ModalWindowConfig config) {
        this.config = config;
        String fxmlFileName = config.getWindowType().getFxmlFileName();
        log.trace("Open [{}] modal window", fxmlFileName);

        Pane modalPane = modalsProvider.loadFxmlFile(fxmlFileName);
        Stage parentStage = config.getParentStage();
        parentRoot = parentStage.getScene().getRoot();

        modalStage = new Stage(StageStyle.TRANSPARENT);
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(parentStage);

        Scene scene = new Scene(modalPane);
        scene.setFill(Color.TRANSPARENT);
        modalStage.setScene(scene);

        addModalitySettings(parentStage);
        addCallbacksSettings();
        addPositionSettings(parentStage);

        modalStage.show();
    }

    public void closeModalWindow() {
        log.trace("Close modal window");
        parentRoot.setEffect(new DropShadow(BlurType.TWO_PASS_BOX, Color.DIMGRAY, 10, 0, 4, 4));
        modalStage.close();

        handleCallback(this.config.getCloseCallback());

        int closeCallbackRepeatDelay = this.config.getCloseCallbackRepeatDelay();
        if (closeCallbackRepeatDelay > 0) {
            log.trace("Repeat closing callback...");
            TaskUtils.startDelayedTask(
                    closeCallbackRepeatDelay,
                    () -> handleCallback(this.config.getCloseCallback())
            );
        }
    }

    private void closeNonErrorModalWindow() {
        log.trace("{}", this.config);
        if (this.config != null && this.config.getWindowType() != ModalWindowType.ERROR_MODAL_WINDOW ) {
            closeModalWindow();
        }
    }

    public void showErrorWindow(Stage stage, String message) {
        log.trace("Show error modal window, message: {}", message);
        ModalWindowConfig config = ModalWindowConfig.builder()
                .windowType(ModalWindowType.ERROR_MODAL_WINDOW)
                .closeOnMaskClick(true)
                .parentStage(stage)
                .errorMessage(message)
                .build();
        closeNonErrorModalWindow();
        openModalWindow(config);
    }

    private void addPositionSettings(Stage parentStage) {
        double centerXPosition = parentStage.getX() + parentStage.getWidth()/2d;
        double centerYPosition = parentStage.getY() + parentStage.getHeight()/2d;

        modalStage.setOnShowing(ev -> {
            modalStage.hide();
        });
        modalStage.setOnShown(event -> {
            modalStage.setX(centerXPosition - modalStage.getWidth()/2d);
            modalStage.setY(centerYPosition - modalStage.getHeight()/2d);
            modalStage.show();
        });
    }

    private void addModalitySettings(Stage parentStage) {
        // it is necessary to combine the effects to get a mask of a modal window + shadows of the main window
        Light.Distant light = new Light.Distant();
        light.setAzimuth(0);
        light.setColor(Color.DIMGRAY);
        light.setElevation(50);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(0);

        DropShadow dropShadow = new DropShadow(BlurType.TWO_PASS_BOX, Color.DIMGRAY, 10, 0, 4, 4);
        dropShadow.setInput(lighting);

        parentStage.getScene().getRoot().setEffect(dropShadow);
    }

    private void addCallbacksSettings() {
        if (this.config.isCloseOnMaskClick()) {
            modalStage.focusedProperty().addListener((ov, onHidden, onShown) -> {
                if (onHidden) {
                    parentRoot.setEffect(new DropShadow(BlurType.TWO_PASS_BOX, Color.DIMGRAY, 10, 0, 4, 4));
                    modalStage.close();
                }
            });
        }
    }

    private void handleCallback(Callable<Boolean> callback) {
        if (callback != null) {
            try {
                callback.call();
            } catch (Exception e) {
                log.error("Error when execute modal window close callback", e);
                throw new ShownOnModalException(ErrorMessage.CANNOT_PERFORM_ACTION);
            }
        }
    }

}
