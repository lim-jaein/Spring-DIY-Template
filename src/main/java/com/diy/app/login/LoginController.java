package com.diy.app.login;

import com.diy.framework.web.Controller;
import com.diy.framework.web.annotation.Component;
import com.diy.framework.web.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequestMapping(value = "/login")
public class LoginController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("loginController test");
        // 인터페이스 방식은 내부에서 http method 분기처리 필요
    }
}
