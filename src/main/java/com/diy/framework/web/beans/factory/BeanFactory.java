package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotation.Autowired;
import com.diy.framework.web.annotation.Component;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 생성된 빈은 구현체 타입과 인터페이스 타입을 키로 Map에 저장되며,
 * getBean 호출 시 이미 생성된 빈을 반환하거나 없으면 새로 생성하여 주입한다.
 */
public class BeanFactory {
    private final BeanScanner beanScanner;
    private final Map<Class<?>, Object> beans = new HashMap<>();

    /**
     * @Component 어노테이션이 붙은 클래스를 스캔하여 인스턴스를 생성하고 보관
     * @param basePackages
     */
    public BeanFactory(String basePackages) {
        beanScanner = new BeanScanner(basePackages);
        beanScanner.scanClassesTypeAnnotatedWith(Component.class)
                .forEach(this::createBean);
    }

    private Object createBean(Class<?> clazz) {
        Object bean;
        try {
            // @Autowired 붙은 생성자 존재 여부 확인
            Constructor<?> autowiredConstructor = findAutowiredConstructor(clazz);
            if (autowiredConstructor == null) {
                bean = clazz.getDeclaredConstructor().newInstance();
            } else {
                Object[] params = Arrays.stream(autowiredConstructor.getParameterTypes())
                        .map(this::getBean)
                        .toArray();
                bean = autowiredConstructor.newInstance(params);
            }
            beans.put(clazz, bean);
            for (Class<?> iFace : clazz.getInterfaces()) {
                beans.put(iFace, bean);
            }
        } catch (Exception e) {
            throw new RuntimeException(clazz.getName() + " 빈 생성 실패", e);
        }
        return bean;
    }

    public Object getBean(Class<?> clazz) {
        Object bean = beans.get(clazz);
        return bean == null ? createBean(clazz) : bean;
    }

    private Constructor<?> findAutowiredConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElse(null);
    }

}
