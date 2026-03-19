package com.diy.framework.web;

import com.diy.app.lecture.LectureController;
import com.diy.framework.web.mapping.ControllerKey;
import com.diy.framework.web.mapping.ControllerMapping;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private final ControllerMapping controllerMapping = new ControllerMapping();

    @Override
    public void init() throws ServletException {
        LectureController lectureController = new LectureController();
        controllerMapping.setController(new ControllerKey("GET", "/lectures"), lectureController);
        controllerMapping.setController(new ControllerKey("POST", "/lectures"), lectureController);
        controllerMapping.setController(new ControllerKey("PUT", "/lectures/{id}"), lectureController);
        controllerMapping.setController(new ControllerKey("DELETE", "/lectures/{id}"), lectureController);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        Map<String, ?> param = parseParam(req);
        Controller controller = controllerMapping.getController(req);
        if (controller == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            controller.handleRequest(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 파라미터 파싱
     * @return
     */
    private Map<String, ?> parseParam(HttpServletRequest req) throws IOException {
        if ("application/json".equals(req.getHeader("Content-Type"))) {
            final byte[] bodyBytes = req.getInputStream().readAllBytes();;
            final String body = new String(bodyBytes, StandardCharsets.UTF_8);

            return new ObjectMapper().readValue(body, new TypeReference<Map<String, Object>>() {});
        } else {
            return req.getParameterMap();
        }
    }
}
