package cool.scx.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识此类是一个需要注入到容器中的类 和 {@link cool.scx.core.annotation.ScxService} 的区别仅在于注解的语义不同
 *
 * @author scx567888
 * @version 1.11.5
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScxComponent {

}
