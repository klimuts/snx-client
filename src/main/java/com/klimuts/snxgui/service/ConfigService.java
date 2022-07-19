package com.klimuts.snxgui.service;

import com.klimuts.snxgui.config.AppConfig;
import com.klimuts.snxgui.model.ConfigKey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigService {

    public static final String APP_SEPARATOR = "=";
    public static final String SNX_SEPARATOR = " ";

    private Map<ConfigKey, String> config;
    private String userHomeDir;

    public Map<ConfigKey, String> getConfig() {
        return config;
    }

    public void initConfig() throws IOException {
        config = new HashMap<>();
        userHomeDir = System.getProperty("user.home");
        initAppConfig();
        initSnxConfig();
    }

    public void saveConfig(Map<ConfigKey, String> newConfig) throws IOException {
        saveAppConfig(newConfig);
        saveSnxConfig(newConfig);
    }

    private void initSnxConfig() throws IOException {
        initConfig(userHomeDir + AppConfig.SNX_CONFIG_PATH, SNX_SEPARATOR);
    }

    private void initAppConfig() throws IOException {
        initConfig(AppConfig.APP_CONFIG_PATH, APP_SEPARATOR);
    }

    private void initConfig(String configPath, String separator) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(configPath));
        lines.forEach(line -> {
            String[] row = line.split(separator);
            ConfigKey key = ConfigKey.findByRawValue(row[0].trim());
            String value = row.length > 1 ? row[1].trim() : "";
            config.put(key, value);
        });
    }

    private void saveAppConfig(Map<ConfigKey, String> newConfig) throws IOException {
        List<String> lines = new ArrayList<>();
        newConfig.forEach((key, value) -> {
            if (!ConfigKey.isSnxKey(key)) {
                lines.add(key.getValue() + APP_SEPARATOR + value);
            }
        });

        saveConfig(lines, AppConfig.APP_CONFIG_PATH);
    }

    private void saveSnxConfig(Map<ConfigKey, String> newConfig) throws IOException {
        List<String> lines = new ArrayList<>();
        newConfig.forEach((key, value) -> {
            if (ConfigKey.isSnxKey(key)) {
                lines.add(key.getValue() + SNX_SEPARATOR + value);
            }
        });
        saveConfig(lines, userHomeDir + AppConfig.SNX_CONFIG_PATH);
    }

    private void saveConfig(List<String> lines, String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        lines.forEach(line -> sb.append(line).append("\n"));
        Files.writeString(Path.of(path), sb.toString());
    }

}
