module com.klimuts.snxgui {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires SystemTray;
    requires java.desktop;

    requires static lombok;

    opens com.klimuts.snxgui to javafx.fxml;
    exports com.klimuts.snxgui;
    exports com.klimuts.snxgui.controller;
    opens com.klimuts.snxgui.controller to javafx.fxml;
    exports com.klimuts.snxgui.service;
    opens com.klimuts.snxgui.service to javafx.fxml;
    exports com.klimuts.snxgui.model;
    opens com.klimuts.snxgui.model to javafx.fxml;
    exports com.klimuts.snxgui.handler;
    opens com.klimuts.snxgui.handler to javafx.fxml;
    exports com.klimuts.snxgui.config;
    opens com.klimuts.snxgui.config to javafx.fxml;
}