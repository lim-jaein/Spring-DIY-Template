package com.diy.framework.web.context;

import com.diy.framework.web.beans.factory.BeanFactory;

public class ApplicationContext {

    private final BeanFactory beanFactory;

    public ApplicationContext(final String basePackages, final Object beanConfig) {
        this.beanFactory = new BeanFactory(basePackages, beanConfig);
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
