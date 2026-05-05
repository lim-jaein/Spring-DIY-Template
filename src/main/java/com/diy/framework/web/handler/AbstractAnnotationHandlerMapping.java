package com.diy.framework.web.handler;

import com.diy.framework.web.annotation.RequestMethod;
import com.diy.framework.web.beans.factory.BeanFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAnnotationHandlerMapping implements HandlerMapping {
    private final Map<HandlerKey, Handler> exactRoutes = new HashMap<>();
    private final Map<HandlerKey, Handler> patternRoutes = new HashMap<>();

    @Override
    public Handler getHandler(HttpServletRequest req) {
        HandlerKey key = new HandlerKey(RequestMethod.valueOf(req.getMethod()), req.getRequestURI());

        if (exactRoutes.containsKey(key)) {
            return exactRoutes.get(key);
        }

        for (HandlerKey patternKey : patternRoutes.keySet()) {
            if (patternKey.matches(key)) {
                return patternRoutes.get(patternKey);
            }
        }
        return null;
    }

    protected void setHandler(HandlerKey key, Handler handler) {
        if (key.getPath().contains("{")) {
            patternRoutes.put(key, handler);
        } else {
            exactRoutes.put(key, handler);
        }
    }

    @Override
    public abstract void register(BeanFactory beanFactory);
}
