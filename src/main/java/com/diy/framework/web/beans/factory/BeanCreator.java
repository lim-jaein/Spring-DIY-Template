package com.diy.framework.web.beans.factory;

import java.util.Map;

public abstract class BeanCreator {
    protected final Map<String, Object> beans;

    BeanCreator(Map<String, Object> beans) {
        this.beans = beans;
    }

    public abstract void createBeans();

    protected Object getBean(Class<?> clazz) {
        return beans.values().stream()
                .filter(b -> clazz.isAssignableFrom(b.getClass()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(clazz.getSimpleName() + " 빈을 찾을 수 없음"));
    }
}
