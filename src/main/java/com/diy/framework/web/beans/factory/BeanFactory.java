package com.diy.framework.web.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 빈 인스턴스를 생성하고 보관하는 객체
 * 생성된 빈은 이름을 키로 Map에 저장되며, getBean 호출 시 반환된다.
 * 생성자에서 @Component 스캔해서 빈 등록
 * 별도 메서드로 @Bean 메서드를 통해 빈 등록
 */
public class BeanFactory {
    private final Map<String, Object> beans = new HashMap<>();

    public BeanFactory(String basePackages) {
        List<BeanCreator> beanCreators = List.of(
                new ComponentBeanCreator(basePackages, beans),      // @Component 스캔
                new NamedBeanCreator(beans)                         // BeanConfig에 등록된 @Bean 메서드 처리
        );

        beanCreators.forEach(BeanCreator::createBeans);
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
}
