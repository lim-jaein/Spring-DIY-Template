package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotation.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * base 패키지를 스캔해 @Component 클래스를 찾고 ComponentBeanDefinition 목록을 생성하는 구현체
 */
public class ComponentBeanDefinitionCreator implements BeanDefinitionCreator {
    private final String basePackages;

    ComponentBeanDefinitionCreator(String basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        BeanScanner beanScanner = new BeanScanner(basePackages);

        return beanScanner.scanClassesTypeAnnotatedWith(Component.class)
                .stream().map(ComponentBeanDefinition::new)
                .collect(Collectors.toList());
    }
}
