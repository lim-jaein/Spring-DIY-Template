package com.diy.framework.web;

import com.diy.framework.web.annotation.RequestMapping;
import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.mapping.ControllerMapping;
import com.diy.framework.web.model.ModelAndView;
import com.diy.framework.web.view.View;
import com.diy.framework.web.view.ViewResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private final ControllerMapping controllerMapping = new ControllerMapping();
    private final ViewResolver viewResolver = new ViewResolver();

    @Override
    public void init() {
        // 최초 빈 등록
        BeanFactory beanFactory = new BeanFactory("com.diy");

        // Controller url 매핑
        controllerMapping.register(beanFactory);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object controller = controllerMapping.getController(req);
        if (controller == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            Method method = findMethod(controller, req);
            ModelAndView modelAndView = (ModelAndView) method.invoke(controller, req, resp);
            render(req, resp, modelAndView);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Method findMethod(Object controller, HttpServletRequest req) {
        for (Method method : controller.getClass().getMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                if (mapping.method().equals(req.getMethod())) {
                    return method;
                }
            }
        }
        throw new RuntimeException("해당 url로 매핑된 메서드가 없습니다: " + req.getMethod() + " " + req.getRequestURI());
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView) throws Exception {
        final String viewName = modelAndView.getViewName();
        final View view = viewResolver.resolve(viewName);

        if (view == null) {
            throw new RuntimeException("View not found: " + viewName);
        } else {
            view.render(req, resp, modelAndView.getModel());
        }
    }
}
