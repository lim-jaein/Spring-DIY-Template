package com.diy.framework.web.handler;

import com.diy.framework.web.annotation.RequestMapping;
import com.diy.framework.web.annotation.RequestMethod;
import com.diy.framework.web.annotation.RestController;
import com.diy.framework.web.beans.factory.BeanFactory;

import java.lang.reflect.Method;

/**
 * @RestController 기반 컨트롤러를 Handler에 매핑
 */
public class RestAnnotationHandlerMapping extends AbstractAnnotationHandlerMapping {

    @Override
    public void register(BeanFactory beanFactory) {
        beanFactory.getBeans(RestController.class).forEach(controller -> {
            for (Method method : controller.getClass().getMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    for (RequestMethod requestMethod : mapping.methods()) {
                        setHandler(new HandlerKey(requestMethod, mapping.value()), new RestAnnotationHandler(controller, method));
                    }
                }
            }
        });
    }
}
