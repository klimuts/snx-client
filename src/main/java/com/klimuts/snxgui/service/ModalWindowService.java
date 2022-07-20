package com.klimuts.snxgui.service;

import com.klimuts.snxgui.di.annotation.Autowired;
import com.klimuts.snxgui.di.annotation.Component;
import com.klimuts.snxgui.exception.ShownOnModalException;
import com.klimuts.snxgui.model.ModalWindowConfig;
import com.klimuts.snxgui.model.enums.ModalWindowType;
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

import java.util.concurrent.Callable;

@Component
public class ModalWindowService {

    @Autowired ModalPaneLoader modalsProvider;

    @Getter
    private String errorMessage;
    private Parent parentRoot;
    private Stage modalStage;

    public void openModalWindow(ModalWindowConfig config) {
        Pane modalPane = modalsProvider.loadFxmlFile(config.getWindowType().getFxmlFileName());
        Stage parentStage = config.getParentStage();
        parentRoot = parentStage.getScene().getRoot();
        errorMessage = config.getErrorMessage();

        modalStage = new Stage(StageStyle.TRANSPARENT);
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(parentStage);

        Scene scene = new Scene(modalPane);
        scene.setFill(Color.TRANSPARENT);
        modalStage.setScene(scene);

        addModalitySettings(parentStage);
        addCallbacksSettings(config);
        addPositionSettings(parentStage);

        modalStage.show();
    }

    public void closeModalWindow() {
        parentRoot.setEffect(new DropShadow(BlurType.TWO_PASS_BOX, Color.DIMGRAY, 10, 0, 4, 4));
        modalStage.close();
    }

    public void showErrorWindow(Stage stage, String message) {
        closeModalWindow();
        ModalWindowConfig config = ModalWindowConfig.builder()
                .windowType(ModalWindowType.ERROR_MODAL_WINDOW)
                .closeOnMaskClick(true)
                .parentStage(stage)
                .errorMessage(message)
                .build();
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

    private void addCallbacksSettings(ModalWindowConfig config) {
        if (config.isCloseOnMaskClick()) {
            modalStage.focusedProperty().addListener((ov, onHidden, onShown) -> {
                if (onHidden) {
                    parentRoot.setEffect(new DropShadow(BlurType.TWO_PASS_BOX, Color.DIMGRAY, 10, 0, 4, 4));
                    modalStage.close();
                }
            });
        }
        modalStage.setOnHidden(event -> handleCallback(config.getCloseCallback()));
    }

    private void handleCallback(Callable<Boolean> callback) {
        if (callback != null) {
            try {
                callback.call();
            } catch (Exception e) {
                throw new ShownOnModalException("SNX Client: cannot execute callback");
            }
        }
    }

}
