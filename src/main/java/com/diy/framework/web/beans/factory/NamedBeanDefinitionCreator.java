package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * config의 @Bean 메서드를 읽어 NamedBeanDefinition 목록을 생성하는 구현체
 */
public class NamedBeanDefinitionCreator implements BeanDefinitionCreator {
    private final Object config;

    NamedBeanDefinitionCreator(Object config) {
        this.config = config;
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return Arrays.stream(config.getClass().getMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .map(method -> new NamedBeanDefinition(method, config))
                .collect(Collectors.toList());
    }
}
