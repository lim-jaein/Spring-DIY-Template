package com.diy.framework.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 클래스에 붙이는 커스텀 어노테이션
 * 해당 어노테이션이 붙은 클래스는 BeanScanner에 의해 스캔되어 Bean으로 등록된다.
 * 리플렉션으로 읽을 수 있도록 RetentionPolicy.RUNTIME으로 설정한다.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
}
