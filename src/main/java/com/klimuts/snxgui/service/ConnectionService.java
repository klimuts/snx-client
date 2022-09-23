package com.klimuts.snxgui.service;

import com.klimuts.snxgui.di.annotation.Autowired;
import com.klimuts.snxgui.di.annotation.Component;
import com.klimuts.snxgui.model.enums.SessionInfoKey;
import com.klimuts.snxgui.model.enums.ShellCommand;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ConnectionService {

    @Autowired private ShellCommandService shellCommandService;
    @Autowired private StateFileService stateFileService;

    public void connect(String passcode) {
        log.trace("Run [{}] command", ShellCommand.CONNECT.name());
        shellCommandService.runShellCommand(ShellCommand.CONNECT, passcode);
    }

    public void disconnect() {
        log.trace("Run [{}] command", ShellCommand.DISCONNECT.name());
        shellCommandService.runShellCommand(ShellCommand.DISCONNECT);
    }

    public boolean isConnected() {
        log.trace("Run [{}] command", ShellCommand.CHECK_TUNNEL_INTERFACE.name());
        String snxTunnel = shellCommandService.runShellCommand(ShellCommand.CHECK_TUNNEL_INTERFACE);
        boolean isConnected = !snxTunnel.isEmpty();
        log.trace(isConnected ? "Tunnel is up: {}" : "Tunnel is down", snxTunnel);
        return isConnected;
    }

    public Map<SessionInfoKey, String> getConnectionInfo() throws IOException {
        log.trace("Start reading [session info] from state file");
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
        log.trace("[session info] successfully read from state file");
        return sessionInfo;
    }

    public String checkConnectionError() throws IOException {
        log.trace("Start reading [snx error] from state file");
        List<String> lines = stateFileService.readStateFile(StateFileService.ERROR_STOP_MARKER);
        if (lines == null) {
            return "";
        }
        log.trace("[snx error] successfully read from state file");
        return String.join("\n", lines);
    }

}
