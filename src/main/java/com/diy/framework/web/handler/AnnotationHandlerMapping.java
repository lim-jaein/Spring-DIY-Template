package com.diy.framework.web.handler;

import com.diy.framework.web.annotation.Controller;
import com.diy.framework.web.annotation.RequestMapping;
import com.diy.framework.web.annotation.RequestMethod;
import com.diy.framework.web.beans.factory.BeanFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Controller 기반 컨트롤러를 Handler에 매핑
 */
public class AnnotationHandlerMapping implements HandlerMapping{
    private final Map<HandlerKey, Handler> exactRoutes = new HashMap<>();
    private final Map<HandlerKey, Handler> patternRoutes = new HashMap<>();

    public Handler getHandler(HttpServletRequest req) {
        HandlerKey key = new HandlerKey(RequestMethod.valueOf(req.getMethod()), req.getRequestURI());

        if(exactRoutes.containsKey(key)) {
            return exactRoutes.get(key);
        }

        // 매핑되는 URL이 없는 경우, path variable 확인 후 404 처리
        for (HandlerKey patternKey : patternRoutes.keySet()) {
            if (patternKey.matches(key)) {
                return patternRoutes.get(patternKey);
            }
        }
        return null;
    }

    public void setHandler(HandlerKey key, Handler handler) {
        if (key.getPath().contains("{")) {
            patternRoutes.put(key, handler);
        } else {
            exactRoutes.put(key, handler);
        }
    }

    public void register(BeanFactory beanFactory) {
        beanFactory.getBeans(Controller.class).forEach(controller -> {
            for (Method method : controller.getClass().getMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    for (RequestMethod requestMethod : mapping.methods()) {
                        setHandler(new HandlerKey(requestMethod, mapping.value()), new AnnotationHandler(controller, method));
                    }
                }
            }
        });
    }
}
