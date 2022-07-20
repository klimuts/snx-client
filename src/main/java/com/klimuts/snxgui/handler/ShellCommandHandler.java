package com.klimuts.snxgui.handler;

import com.klimuts.snxgui.exception.ShownOnModalException;
import com.klimuts.snxgui.model.enums.ShellCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShellCommandHandler {

    public String runShellCommand(ShellCommand command) {
        return runShellCommand(command, null);
    }

    public String runShellCommand(ShellCommand command, String arg) {
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
            } else {
                collectShellOutput(output, process.getErrorStream());
                collectShellOutput(output, process.getInputStream());
            }
        } catch (IOException | InterruptedException e) {
            throw new ShownOnModalException("SNX Client: cannot execute shell command");
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
