package cool.scx.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ScxWebSocketRoute
 * websocket 映射
 * 设置此注解的方法 必须同时 继承 BaseWSHandler 才可以生效
 *
 * @author scx567888
 * @version 0.0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScxWebSocketRoute {

    /**
     * websocket 映射路径
     *
     * @return 映射路径
     */
    String value();

    /**
     * 排序
     *
     * @return a
     */
    int order() default 0;

}
