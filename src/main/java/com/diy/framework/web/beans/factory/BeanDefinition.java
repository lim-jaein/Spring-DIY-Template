package com.diy.framework.web.beans.factory;

/**
 * 빈 등록에 필요한 메타데이터를 정의하는 인터페이스
 * @Component 방식과 @Bean 방식의 공통 타입으로 사용되며,
 * BeanFactory가 빈을 생성할 때 이름을 조회하는 용도로 쓰인다
 */
public interface BeanDefinition {
    public String getBeanName();
}
