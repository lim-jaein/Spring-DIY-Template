package com.diy.framework.web.handler;

import com.diy.framework.web.Controller;
import com.diy.framework.web.model.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InterfaceHandler implements Handler {
    private final Controller controller;

    public InterfaceHandler(Controller controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        controller.handleRequest(request, response);
        return null;
    }
}
