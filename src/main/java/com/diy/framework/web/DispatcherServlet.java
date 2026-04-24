package com.diy.framework.web;

import com.diy.framework.web.beans.factory.BeanFactory;
import com.diy.framework.web.context.ApplicationContext;
import com.diy.framework.web.handler.AnnotationHandlerMapping;
import com.diy.framework.web.handler.Handler;
import com.diy.framework.web.handler.HandlerMapping;
import com.diy.framework.web.handler.InterfaceHandlerMapping;
import com.diy.framework.web.model.ModelAndView;
import com.diy.framework.web.view.View;
import com.diy.framework.web.view.ViewResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private final List<HandlerMapping> handlerMappings;
    private final ViewResolver viewResolver = new ViewResolver();

    DispatcherServlet() {
        handlerMappings = List.of(
                new AnnotationHandlerMapping(),
                new InterfaceHandlerMapping()
        );
    }

    @Override
    public void init() {
        ApplicationContext applicationContext =
                (ApplicationContext) getServletContext().getAttribute("applicationContext");

        BeanFactory beanFactory = applicationContext.getBeanFactory();
        handlerMappings.forEach(handlerMapping -> handlerMapping.register(beanFactory));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Handler handler = handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(req))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        if (handler == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            ModelAndView modelAndView = handler.handleRequest(req, resp);
            if (modelAndView != null) {
                render(req, resp, modelAndView);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
