package com.klimuts.snxgui.di;

import com.klimuts.snxgui.handler.ShellCommandHandler;
import com.klimuts.snxgui.service.*;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private static Map<Class<?>, Object> beans;

    public static void init() {
        beans = new HashMap<>();

        beans.put(ModalWindowService.class, new ModalWindowService());
        beans.put(ShellCommandHandler.class, new ShellCommandHandler());
        beans.put(ModalPaneLoader.class, new ModalPaneLoader());
        beans.put(StateFileService.class, new StateFileService());
        beans.put(ConnectionService.class, new ConnectionService(getBean(ShellCommandHandler.class), getBean(StateFileService.class)));
        beans.put(ConfigService.class, new ConfigService());
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<?> clazz) {
        return (T) beans.get(clazz);
    }

}
