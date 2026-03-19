package com.diy.framework.web.mapping;

import java.util.Objects;

public class ControllerKey {
    private final String method;
    private final String path;

    public ControllerKey(String method, String path) {
        this.method = method;
        this.path = path;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ControllerKey other)) return false;

        return this.method.equals(other.method) && this.path.equals(other.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path);
    }

    public boolean matches(ControllerKey other) {
        return this.method.equals(other.method) && other.path.matches(this.path.replaceAll("\\{[^}]+}", "[0-9]+"));
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
