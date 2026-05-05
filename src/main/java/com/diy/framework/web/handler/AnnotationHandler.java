package com.diy.framework.web.handler;

import com.diy.framework.web.model.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AnnotationHandler extends AbstractAnnotationHandler {

    public AnnotationHandler(Object controller, Method method) {
        super(controller, method);
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return (ModelAndView) method.invoke(controller, resolveParameters(req, resp));
    }
}
