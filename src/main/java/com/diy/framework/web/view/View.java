package com.diy.framework.web.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
    public void render(final HttpServletRequest req, final HttpServletResponse resp) throws Exception;
}
