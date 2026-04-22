package com.diy.framework.web.beans.factory;

/**
 * @Component 방식으로 등록되는 빈의 메타데이터
 * BeanFactory가 reflection으로 객체를 생성할 때 필요한 Class<?> 정보를 보관한다
 */
public class ComponentBeanDefinition implements BeanDefinition{
    private final Class<?> clazz;

    public ComponentBeanDefinition(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getBeanName() {
        String name = clazz.getSimpleName();
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    public Class<?> getType() { return clazz; }
}
