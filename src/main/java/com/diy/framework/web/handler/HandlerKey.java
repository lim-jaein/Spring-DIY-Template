package com.diy.framework.web.handler;

import com.diy.framework.web.annotation.RequestMethod;

import java.util.Objects;

public class HandlerKey {
    private final RequestMethod method;
    private final String path;

    public HandlerKey(RequestMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HandlerKey other)) return false;

        return Objects.equals(this.method, other.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path);
    }

    public boolean matches(HandlerKey other) {
        return this.method.equals(other.method) && other.path.matches(this.path.replaceAll("\\{[^}]+}", "[0-9]+"));
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
