package com.klimuts.snxgui.model.enums;

public enum SessionInfoKey {

    OFFICE_MODE_IP("Office Mode IP"),
    DNS_SERVER("DNS Server"),
    SECONDARY_DNS_SERVER("Secondary DNS Server"),
    DNS_SUFFIX("DNS Suffix"),
    TIMEOUT("Timeout");

    private final String rawValue;

    SessionInfoKey(String rawValue) {
        this.rawValue = rawValue;
    }

    public String getValue() {
        return this.rawValue;
    }

    public static SessionInfoKey findByRawValue(String rawValue) {
        for (SessionInfoKey v : values()) {
            if (v.getValue().equals(rawValue)) {
                return v;
            }
        }
        return null;
    }

}
