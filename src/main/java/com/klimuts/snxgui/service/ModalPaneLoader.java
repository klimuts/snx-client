package com.klimuts.snxgui.service;

import com.klimuts.snxgui.SnxClient;
import com.klimuts.snxgui.di.Context;
import com.klimuts.snxgui.di.ContextInitializer;
import com.klimuts.snxgui.di.annotation.Autowired;
import com.klimuts.snxgui.di.annotation.Component;
import com.klimuts.snxgui.exception.ShownOnModalException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

@Component
public class ModalPaneLoader {

    @Autowired private Context context;

    public Pane loadFxmlFile(String fileName) {
        try {
            URL fileUrl = SnxClient.class.getResource(fileName + ".fxml");
            FXMLLoader loader = new FXMLLoader(fileUrl);
            loader.setControllerFactory(context::getBean);

            return loader.load();
        } catch (Exception e) {
            throw new ShownOnModalException("SNX Client: cannot load fxml layout");
        }
    }

}
