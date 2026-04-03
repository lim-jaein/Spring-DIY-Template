package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotation.Autowired;
import com.diy.framework.web.annotation.Component;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Map;

public class ComponentBeanCreator extends BeanCreator {
    private final String basePackages;

    ComponentBeanCreator(String basePackages, Map<String, Object> beans) {
        super(beans);
        this.basePackages = basePackages;
    }

    @Override
    public void createBeans() {
        BeanScanner beanScanner = new BeanScanner(basePackages);

        beanScanner.scanClassesTypeAnnotatedWith(Component.class)
                .forEach(this::createBean);
    }

    private void createBean(Class<?> clazz) {
        Object bean;
        String className;
        try {
            Constructor<?> autowiredConstructor = findAutowiredConstructor(clazz);

            if (autowiredConstructor == null) {
                bean = clazz.getDeclaredConstructor().newInstance();
            } else {
                Object[] params = Arrays.stream(autowiredConstructor.getParameterTypes())
                        .map(this::getBean)
                        .toArray();
                bean = autowiredConstructor.newInstance(params);
            }
            className = clazz.getSimpleName();
            className = Character.toLowerCase(className.charAt(0)) + className.substring(1);
            beans.put(className, bean);
        } catch (Exception e) {
            throw new RuntimeException(clazz.getName() + " 빈 생성 실패", e);
        }
    }

    private Constructor<?> findAutowiredConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElse(null);
    }
}
