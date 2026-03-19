package com.diy.framework.web.mapping;

import com.diy.framework.web.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ControllerMapping {
    private final Map<ControllerKey, Controller> exactRoutes = new HashMap<>();
    private final Map<ControllerKey, Controller> patternRoutes = new HashMap<>();

    public Controller getController(HttpServletRequest req) {
        ControllerKey key = new ControllerKey(req.getMethod(), req.getRequestURI());

        if(exactRoutes.containsKey(key)) {
            return exactRoutes.get(key);
        }

        // 매핑되는 URL이 없는 경우, path variable 확인 후 404 처리
        for (ControllerKey patternKey : patternRoutes.keySet()) {
            if (patternKey.matches(key)) {
                return patternRoutes.get(patternKey);
            }
        }
        return null;
    }

    public void setController(ControllerKey key, Controller controller) {
        if (key.getPath().contains("{")) {
            patternRoutes.put(key, controller);
        } else {
            exactRoutes.put(key, controller);
        }
    }
}
