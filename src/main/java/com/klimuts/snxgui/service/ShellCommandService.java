package com.klimuts.snxgui.service;

import com.klimuts.snxgui.exception.ErrorMessage;
import com.klimuts.snxgui.exception.ShownOnModalException;
import com.klimuts.snxgui.model.enums.ShellCommand;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class ShellCommandService {

    public String runShellCommand(ShellCommand command) {
        return runShellCommand(command, null);
    }

    public String runShellCommand(ShellCommand command, String arg) {
        log.debug("Execute shell command: [{}: {}]", command.name(), command.value());
        String cmd;
        if (arg != null && !arg.isEmpty()) {
            cmd = String.format(command.value(), arg);
        } else {
            cmd = command.value();
        }
        StringBuilder output = new StringBuilder();
        try {
            Process process = new ProcessBuilder()
                    .command("sh", "-c", cmd)
                    .start();

            int exitVal = process.waitFor();

            if (exitVal == 0) {
                collectShellOutput(output, process.getInputStream());
                log.debug("Command [{}] executed successfully, exit code: 0", command);
            } else {
                collectShellOutput(output, process.getErrorStream());
                collectShellOutput(output, process.getInputStream());
                log.debug("Command [{}] failed, exit code: {}}", command, exitVal);
            }

        } catch (IOException | InterruptedException e) {
            log.error("Error when execute shell command", e);
            throw new ShownOnModalException(ErrorMessage.CANNOT_PERFORM_ACTION);
        }
        return output.toString();
    }

    private void collectShellOutput(StringBuilder output, InputStream is) throws IOException {
        BufferedReader out = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = out.readLine()) != null) {
            output.append(line);
        }
    }

}
