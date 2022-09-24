open module com.klimuts.snxgui {
    requires static lombok;

    requires java.desktop;

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires slf4j.api;
    requires SystemTray;
    requires kotlin.stdlib;
    requires io.github.classgraph;
    requires io.github.toolfactory.narcissus;
    requires io.github.toolfactory.jvm;

    exports com.klimuts.snxgui;
    exports com.klimuts.snxgui.controller;
    exports com.klimuts.snxgui.service;
    exports com.klimuts.snxgui.model;
    exports com.klimuts.snxgui.config;
    exports com.klimuts.snxgui.model.enums;
    exports com.klimuts.snxgui.exception;
}