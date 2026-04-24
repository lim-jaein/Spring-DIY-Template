package com.diy.framework.web.handler;

import com.diy.framework.web.annotation.RequestMethod;
import com.diy.framework.web.beans.factory.BeanFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 요청(URL + HTTP 메서드)을 Handler에 매핑하는 인터페이스
 */
public interface HandlerMapping {
    void register(BeanFactory beanFactory);
    Handler getHandler(HttpServletRequest req);
}
