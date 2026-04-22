package com.diy.framework.web.beans.factory;

import java.util.List;

/**
 * BeanDefinition 목록을 생성하는 인터페이스
 * 빈 등록 방식(@Component, @Bean)별로 구현체가 존재하며,
 * BeanFactory가 이를 통해 BeanDefinition 목록을 수집한다
 */
public interface BeanDefinitionCreator {
    // 스캔 또는 설정을 읽어 BeanDefinition 목록 반환
    public List<BeanDefinition> getBeanDefinitions();
}
