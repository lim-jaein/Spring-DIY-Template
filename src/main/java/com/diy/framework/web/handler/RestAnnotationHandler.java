package com.diy.framework.web.handler;

import com.diy.framework.web.model.ModelAndView;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class RestAnnotationHandler extends AbstractAnnotationHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RestAnnotationHandler(Object controller, Method method) {
        super(controller, method);
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Object result = method.invoke(controller, resolveParameters(req, resp));
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(objectMapper.writeValueAsString(result));
        return null;
    }
}
