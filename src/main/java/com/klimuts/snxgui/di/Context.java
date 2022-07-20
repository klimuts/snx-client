package com.klimuts.snxgui.di;

import com.klimuts.snxgui.di.annotation.Component;

import java.util.Map;

@Component
public class Context {

    private final Map<Class<?>, Object> beans;

    public Context(Map<Class<?>, Object> beans) {
        this.beans = beans;
    }

    public void addBean(Class<?> clazz, Object bean) {
        beans.put(clazz, bean);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<?> clazz) {
        return (T) beans.get(clazz);
    }

}
