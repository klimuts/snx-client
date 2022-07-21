package com.klimuts.snxgui.service;

import com.klimuts.snxgui.config.AppConfig;
import com.klimuts.snxgui.di.annotation.Component;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class StateFileService {

    public static final String SUCCESS_STOP_MARKER = "===================";
    public static final String ERROR_STOP_MARKER = "";
    public static final String DATA_SEPARATOR = ":";

    public List<String> readStateFile(String stopMarker) throws IOException {
        List<String> source = Files.readAllLines(Path.of(AppConfig.STATE_PATH));
        if (source.isEmpty()) {
            log.trace("State file is empty");
            return null;
        }
        List<String> lines = new ArrayList<>(source);
        Iterator<String> i = lines.iterator();
        while (i.hasNext()) {
            String s = i.next();
            if (stopMarker.equals(s)) {
                i.remove();
                break;
            } else {
                i.remove();
            }
        }
        if (lines.isEmpty()) {
            log.trace("The status file seems to contain a multi-line SNX error");
            return Collections.singletonList(source.get(0));
        }
        return lines;
    }

    public void clearStateFile() throws IOException {
            Files.writeString(Path.of(AppConfig.STATE_PATH), "");
    }

}
