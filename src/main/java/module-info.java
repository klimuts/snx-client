open module com.klimuts.snxgui {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires SystemTray;
    requires java.desktop;

    requires io.github.classgraph;
    requires io.github.toolfactory.narcissus;
    requires io.github.toolfactory.jvm;

    requires static lombok;

    exports com.klimuts.snxgui;
    exports com.klimuts.snxgui.controller;
    exports com.klimuts.snxgui.service;
    exports com.klimuts.snxgui.model;
    exports com.klimuts.snxgui.config;
    exports com.klimuts.snxgui.model.enums;
}