package com.klimuts.snxgui.model;

public enum ConfigKey {
    // SNX
    SERVER_ADDRESS("server"),
    LOGIN("username"),
    ENABLE_DEBUG("debug"),

    // APP
    DISCONNECT_ON_EXIT("disconnectOnExit");

    private final String rawValue;

    ConfigKey(String rawValue) {
        this.rawValue = rawValue;
    }

    public String getValue() {
        return this.rawValue;
    }

    public static ConfigKey findByRawValue(String rawValue) {
        for (ConfigKey v : values()) {
            if (v.getValue().equals(rawValue)) {
                return v;
            }
        }
        return null;
    }

    public static boolean isSnxKey(ConfigKey key) {
        return key != ConfigKey.DISCONNECT_ON_EXIT;
    }

}
