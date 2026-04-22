package com.diy.framework.web.beans.factory;

import com.diy.framework.web.annotation.Bean;
import java.lang.reflect.Method;

/**
 * @Bean 방식으로 등록되는 빈의 메타데이터
 * BeanFactory가 빈을 생성할 때 필요한 @Bean 메서드와 config 정보를 보관한다
 */
public class NamedBeanDefinition implements BeanDefinition{
    private final Method method;
    private final Object config;

    public NamedBeanDefinition(Method method, Object config) {
        this.method = method;
        this.config = config;
    }
    
    @Override
    public String getBeanName() {
        return method.getAnnotation(Bean.class).value();
    }

    public Method getMethod() { return method; }

    public Object getConfig() { return config; }
}
