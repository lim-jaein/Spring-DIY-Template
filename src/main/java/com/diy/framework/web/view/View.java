package com.diy.framework.web.view;

import com.diy.framework.web.model.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
    public void render(final HttpServletRequest req, final HttpServletResponse resp, final Model model) throws Exception;
}
