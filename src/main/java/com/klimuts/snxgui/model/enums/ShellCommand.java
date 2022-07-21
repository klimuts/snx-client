package com.klimuts.snxgui.model.enums;

public enum ShellCommand {

    CONNECT("x-terminal-emulator -e sh -c 'bash state.sh %s';"),
    DISCONNECT("snx -d 2>&1 | tee state"),
    CHECK_TUNNEL_INTERFACE("ip address | grep tunsnx || true");

    ShellCommand(String value) {
        this.value = value;
    }

    private final String value;

    public String value() {
        return value;
    }

}
