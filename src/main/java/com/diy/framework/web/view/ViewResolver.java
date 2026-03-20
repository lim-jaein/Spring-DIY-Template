package com.diy.framework.web.view;

public class ViewResolver {
    public View resolve(String viewName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (classLoader.getResource(viewName + ".jsp") != null) {
            return new JspView("/" + viewName + ".jsp");
        }

        if (classLoader.getResource(viewName + ".html") != null) {
            return new HtmlView("/" + viewName + ".html");
        }

        throw new IllegalArgumentException("View를 찾을 수 없습니다: " + viewName);
    }
}
