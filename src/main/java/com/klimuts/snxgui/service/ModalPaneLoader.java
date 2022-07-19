package com.klimuts.snxgui.service;

import com.klimuts.snxgui.SnxClient;
import com.klimuts.snxgui.exception.ShownOnModalException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

public class ModalPaneLoader {

    public Pane loadFxmlFile(String fileName) {
        try {
            URL fileUrl = SnxClient.class.getResource(fileName + ".fxml");
            return new FXMLLoader(fileUrl).load();
        } catch (Exception e) {
            throw new ShownOnModalException("SNX Client: cannot load fxml layout");
        }
    }

}
