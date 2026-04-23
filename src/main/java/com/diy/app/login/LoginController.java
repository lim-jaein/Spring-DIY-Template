package com.diy.app.login;

import com.diy.framework.web.Controller;
import com.diy.framework.web.annotation.Component;
import com.diy.framework.web.annotation.RequestMapping;
import com.diy.framework.web.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequestMapping(methods = RequestMethod.GET, value = "/login")
public class LoginController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("loginController test");
    }
}
