package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BeanDefinition 목록을 수집하고, 실제 빈 인스턴스를 생성해 보관하는 객체
 * 생성된 빈은 이름을 키로 Map에 저장되며, getBean 호출 시 반환된다
 */
public class BeanFactory {
    private final Map<String, Object> beans = new HashMap<>();

    public BeanFactory(String basePackages, Object beanConfig) {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        beanDefinitions.addAll(new ComponentBeanDefinitionCreator(basePackages).getBeanDefinitions());
        beanDefinitions.addAll(new NamedBeanDefinitionCreator(beanConfig).getBeanDefinitions());

        createBeans(beanDefinitions);
    }

    // BeanDefinition 목록을 순회하며 타입에 따라 빈을 생성하고 Map에 저장
    public void createBeans(List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Object bean;

            if (beanDefinition instanceof ComponentBeanDefinition componentBeanDefinition) {
                bean = createComponentBean(componentBeanDefinition);
            } else if (beanDefinition instanceof NamedBeanDefinition namedBeanDefinition) {
                bean = createNamedBean(namedBeanDefinition);
            } else {
                throw new RuntimeException("알 수 없는 BeanDefinition 타입: " + beanDefinition.getClass());
            }

            beans.put(beanDefinition.getBeanName(), bean);
        }
    }

    // @Component 방식: @Autowired 생성자가 있으면 의존성 주입, 없으면 기본 생성자로 생성
    private Object createComponentBean(ComponentBeanDefinition definition) {
        Class<?> clazz = definition.getType();
        try {
            Constructor<?> autowiredConstructor = findAutowiredConstructor(clazz);
            if (autowiredConstructor == null) {
                return clazz.getDeclaredConstructor().newInstance();
            }
            Object[] params = Arrays.stream(autowiredConstructor.getParameterTypes())
                    .map(this::getBean)
                    .toArray();
            return autowiredConstructor.newInstance(params);
        } catch (Exception e) {
            throw new RuntimeException(clazz.getName() + " 빈 생성 실패", e);
        }
    }

    // @Bean 방식: config 객체에서 메서드를 invoke해 빈 생성, 파라미터는 타입으로 조회
    private Object createNamedBean(NamedBeanDefinition definition) {
        Method method = definition.getMethod();
        try {
            Object[] params = Arrays.stream(method.getParameterTypes())
                    .map(this::getBean)
                    .toArray();
            return method.invoke(definition.getConfig(), params);
        } catch (Exception e) {
            throw new RuntimeException(method.getName() + " 빈 생성 실패", e);
        }
    }

    // 이름으로 빈 조회
    public Object getBean(String name) {
        return beans.get(name);
    }

    // 어노테이션으로 빈 조회
    public Collection<Object> getBeans(Class<? extends Annotation> annotation) {
        return beans.values().stream()
                .filter(b -> b.getClass().isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    // 타입으로 빈 조회
    protected Object getBean(Class<?> clazz) {
        return beans.values().stream()
                .filter(b -> clazz.isAssignableFrom(b.getClass()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(clazz.getSimpleName() + " 빈을 찾을 수 없음"));
    }

    // 타입으로 빈 리스트 조회
    public List<Object> getBeansByType(Class<?> clazz) {
        return beans.values().stream()
                .filter(b -> clazz.isAssignableFrom(b.getClass()))
                .collect(Collectors.toList());
    }

    private Constructor<?> findAutowiredConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElse(null);
    }
}
