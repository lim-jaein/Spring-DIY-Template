package com.diy.framework.web.view;

import com.diy.framework.web.model.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JspView implements View{
    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final HttpServletRequest req, final HttpServletResponse res, final Model model) throws ServletException, IOException {
        final RequestDispatcher requestDispatcher = req.getRequestDispatcher(viewName);
        model.getAttribute().forEach(req::setAttribute);
        requestDispatcher.forward(req, res);
    }
}
