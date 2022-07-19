package com.klimuts.snxgui.model;

public enum ModalWindowType {

    PASSWORD_MODAL_WINDOW("password"),
    ABOUT_MODAL_WINDOW("about"),
    SETTINGS_MODAL_WINDOW("config"),
    SESSION_MODAL_WINDOW("session"),
    ERROR_MODAL_WINDOW("error");


    ModalWindowType(String fxmlFileName) {
        this.fxmlFileName = fxmlFileName;
    }

    private final String fxmlFileName;

    public String getFxmlFileName() {
        return fxmlFileName;
    }

}
