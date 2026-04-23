package com.diy.framework.web.mapping;

import com.diy.framework.web.annotation.Controller;
import com.diy.framework.web.annotation.RequestMapping;
import com.diy.framework.web.annotation.RequestMethod;
import com.diy.framework.web.beans.factory.BeanFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ControllerMapping {
    private final Map<ControllerKey, Object> exactRoutes = new HashMap<>();
    private final Map<ControllerKey, Object> patternRoutes = new HashMap<>();

    public Object getController(HttpServletRequest req) {
        ControllerKey key = new ControllerKey(RequestMethod.valueOf(req.getMethod()), req.getRequestURI());

        if(exactRoutes.containsKey(key)) {
            return exactRoutes.get(key);
        }

        // 매핑되는 URL이 없는 경우, path variable 확인 후 404 처리
        for (ControllerKey patternKey : patternRoutes.keySet()) {
            if (patternKey.matches(key)) {
                return patternRoutes.get(patternKey);
            }
        }
        return null;
    }

    public void setController(ControllerKey key, Object controller) {
        if (key.getPath().contains("{")) {
            patternRoutes.put(key, controller);
        } else {
            exactRoutes.put(key, controller);
        }
    }

    public void register(BeanFactory beanFactory) {
        // 애너테이션 기반 컨트롤러 매핑
        beanFactory.getBeans(Controller.class).forEach(controller -> {
            for (Method method : controller.getClass().getMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    for (RequestMethod requestMethod : mapping.methods()) {
                        setController(new ControllerKey(requestMethod, mapping.value()), controller);
                    }
                }
            }
        });

        // 인터페이스 기반 컨트롤러 매핑
        beanFactory.getBeansByType(com.diy.framework.web.Controller.class)
                .forEach(controller -> {
                    Class<?> clazz = controller.getClass();
                    if (clazz.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping mapping = clazz.getAnnotation(RequestMapping.class);
                        for (RequestMethod requestMethod : mapping.methods()) {
                            setController(new ControllerKey(requestMethod, mapping.value()), controller);
                        }
                    }
                });
    }
}
