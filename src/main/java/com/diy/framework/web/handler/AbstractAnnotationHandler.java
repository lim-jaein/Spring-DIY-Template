package com.diy.framework.web.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public abstract class AbstractAnnotationHandler implements Handler {
    protected final Object controller;
    protected final Method method;
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected AbstractAnnotationHandler(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    protected Object[] resolveParameters(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] params = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == HttpServletRequest.class) {
                params[i] = req;
            } else if (parameterTypes[i] == HttpServletResponse.class) {
                params[i] = resp;
            } else {
                // JSON 역직렬화
                params[i] = objectMapper.readValue(req.getReader(), parameterTypes[i]);
            }
        }

        return params;
    }
}
