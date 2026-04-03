package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotation.Bean;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class NamedBeanCreator extends BeanCreator {
    private final BeanConfig beanConfig = new BeanConfig();

    NamedBeanCreator(Map<String, Object> beans) {
        super(beans);
    }

    @Override
    public void createBeans() {
        Method[] methods = beanConfig.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Bean.class)) {
                createBean(method);
            }
        }
    }

    private void createBean(Method method) {
        try {
            Object[] params = Arrays.stream(method.getParameterTypes())
                    .map(this::getBean)
                    .toArray();
            Object bean = method.invoke(beanConfig, params);
            String name = method.getAnnotation(Bean.class).value();
            beans.put(name, bean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
