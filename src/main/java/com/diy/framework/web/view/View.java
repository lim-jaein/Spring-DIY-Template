package com.diy.framework.web.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface View {
    public void render(final HttpServletRequest req, final HttpServletResponse resp, final Map<String, Object> model) throws Exception;
}
