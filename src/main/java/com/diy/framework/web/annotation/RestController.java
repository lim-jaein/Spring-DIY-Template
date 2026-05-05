package com.diy.framework.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 클래스에 붙이는 커스텀 어노테이션
 * 해당 어노테이션이 붙은 클래스는 JSON 응답을 제공한다.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestController {
}
