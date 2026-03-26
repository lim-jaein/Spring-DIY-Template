package study.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method에 붙이는 커스텀 어노테이션
 * 리플렉션은 런타임에 동작하므로, RetentionPolicy.RUNTIME으로 설정해야 어노테이션 정보를 읽을 수 있다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrintView {
}
