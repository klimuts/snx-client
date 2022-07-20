package com.klimuts.snxgui.service;

import com.klimuts.snxgui.di.annotation.Autowired;
import com.klimuts.snxgui.di.annotation.Component;
import com.klimuts.snxgui.model.enums.SessionInfoKey;
import com.klimuts.snxgui.model.enums.ShellCommand;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConnectionService {

    @Autowired private ShellCommandService shellCommandService;
    @Autowired private StateFileService stateFileService;

    public void connect(String passcode) {
        shellCommandService.runShellCommand(ShellCommand.CONNECT, passcode);
    }

    public void disconnect() {
        shellCommandService.runShellCommand(ShellCommand.DISCONNECT);
    }

    public boolean isConnected() {
        String snxConnect = shellCommandService.runShellCommand(ShellCommand.CHECK_TUNNEL_INTERFACE);
        return !snxConnect.isEmpty();
    }

    public Map<SessionInfoKey, String> getConnectionInfo() throws IOException {
        List<String> lines = stateFileService.readStateFile(StateFileService.SUCCESS_STOP_MARKER);
        if (lines == null) {
            return null;
        }
        Map<SessionInfoKey, String> sessionInfo = new HashMap<>();
        lines.forEach(line -> {
            SessionInfoKey key = SessionInfoKey.findByRawValue(line.split(StateFileService.DATA_SEPARATOR)[0].trim());
            String value = line.split(StateFileService.DATA_SEPARATOR)[1].trim();
            sessionInfo.put(key, value);
        });

        return sessionInfo;
    }

    public String checkConnectionError() throws IOException {
        List<String> lines = stateFileService.readStateFile(StateFileService.ERROR_STOP_MARKER);
        if (lines == null) {
            return "";
        }
        return String.join("\n", lines);
    }

}
