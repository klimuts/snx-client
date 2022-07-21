package com.klimuts.snxgui.service;

import com.klimuts.snxgui.SnxClient;
import com.klimuts.snxgui.exception.ErrorMessage;
import com.klimuts.snxgui.di.Context;
import com.klimuts.snxgui.di.annotation.Autowired;
import com.klimuts.snxgui.di.annotation.Component;
import com.klimuts.snxgui.exception.ShownOnModalException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;

@Slf4j
@Component
public class ModalPaneLoader {

    @Autowired private Context context;

    public Pane loadFxmlFile(String fileName) {
        try {
            log.trace("Load modal window pane {}.fxml", fileName);
            URL fileUrl = SnxClient.class.getResource(fileName + ".fxml");
            FXMLLoader loader = new FXMLLoader(fileUrl);
            loader.setControllerFactory(context::getBean);

            return loader.load();
        } catch (Exception e) {
            log.error("Error when load {}.fxml layout", fileName);
            throw new ShownOnModalException(ErrorMessage.CANNOT_SHOW_WINDOW);
        }
    }

}
