package com.diy.framework.web.handler;

import com.diy.framework.web.Controller;
import com.diy.framework.web.annotation.RequestMapping;
import com.diy.framework.web.beans.factory.BeanFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller 인터페이스 기반 컨트롤러를 Handler에 매핑
 */
public class InterfaceHandlerMapping implements HandlerMapping {
    private final Map<String, Handler> handlers = new HashMap<>();

    @Override
    public Handler getHandler(HttpServletRequest req) {
        return handlers.get(req.getRequestURI());
    }

    @Override
    public void register(BeanFactory beanFactory) {
        beanFactory.getBeansByType(Controller.class).forEach(bean -> {
            Class<?> clazz = bean.getClass();
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                String url = clazz.getAnnotation(RequestMapping.class).value();
                handlers.put(url, new InterfaceHandler((Controller) bean));
            }
        });
    }

}
