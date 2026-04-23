package com.diy.framework.web;

import com.diy.framework.web.annotation.RequestMapping;
import com.diy.framework.web.annotation.RequestMethod;
import com.diy.framework.web.context.ApplicationContext;
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
import java.util.List;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private final ControllerMapping controllerMapping = new ControllerMapping();
    private final ViewResolver viewResolver = new ViewResolver();

    @Override
    public void init() {
        ApplicationContext applicationContext =
                (ApplicationContext) getServletContext().getAttribute("applicationContext");

        controllerMapping.register(applicationContext.getBeanFactory());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Object controller = controllerMapping.getController(req);
        if (controller == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            // 인터페이스 방식
            if (controller instanceof Controller) {
                ((Controller) controller).handleRequest(req, resp);
            } else {
                // 애너테이션 방식
                Method method = findMethod(controller, req);
                ModelAndView modelAndView = (ModelAndView) method.invoke(controller, req, resp);
                render(req, resp, modelAndView);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Method findMethod(Object controller, HttpServletRequest req) {
        for (Method method : controller.getClass().getMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                RequestMethod requestMethod = RequestMethod.valueOf(req.getMethod());
                if (List.of(mapping.methods()).contains(requestMethod)) {
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
