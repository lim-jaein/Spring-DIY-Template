package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotation.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Component 어노테이션이 붙은 클래스를 스캔하여 인스턴스를 생성하고 보관하는 객체
 * 생성된 빈은 타입을 키로 Map에 저장되며, 필요한 곳에서 꺼내 의존성을 주입할 때 사용된다.
 */
public class BeanFactory {
    private BeanScanner beanScanner;
    private Map<Class<?>, Object> beans = new HashMap<>();

    public BeanFactory(String basePackages) {
        beanScanner = new BeanScanner(basePackages);
        init();
    }

    private void init() {
        Set<Class<?>> classes = beanScanner.scanClassesTypeAnnotatedWith(Component.class);

        for (Class<?> clazz : classes) {
            try {
                Object bean = clazz.getDeclaredConstructor().newInstance();
                beans.put(clazz, bean);
            } catch (Exception e) {
                throw new RuntimeException(clazz.getName() + " 빈 생성 실패", e);
            }
        }
    }
}
