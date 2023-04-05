open module com.klimuts.snxgui {
    requires static lombok;

    requires java.desktop;
    requires java.naming; // fix https://jira.qos.ch/browse/LOGBACK-1515

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires dorkbox.systemtray;
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