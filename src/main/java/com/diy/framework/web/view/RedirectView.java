package com.diy.framework.web.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class RedirectView implements View{
    private final String url;

    public RedirectView(String url) {
        this.url = url;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> model) throws Exception {
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER); // 303
        resp.setHeader("Location", url);
    }
}
