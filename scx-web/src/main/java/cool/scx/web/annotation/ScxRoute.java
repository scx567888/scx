package cool.scx.web.annotation;

import cool.scx.http.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static cool.scx.common.constant.AnnotationValue.NULL;

/**
 * 路由注解 默认会在方法上进行继承 如果想在子类禁用 请使用 NoScxRoute
 *
 * @author scx567888
 * @version 0.3.6
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ScxRoute {

    /**
     * path 路径
     *
     * @return the path
     */
    String value() default NULL;

    /**
     * 方法 默认所有
     *
     * @return the path
     */
    HttpMethod[] methods() default {};

    /**
     * 排序
     *
     * @return a
     */
    int order() default 0;

    /**
     * 是否忽略类级别上的 ScxRoute 注解所定义的顶级 url
     *
     * @return 是否
     */
    boolean ignoreParentUrl() default false;

    /**
     * 注意 : 仅当作用于方法时生效 !!!
     * 是否使用方法名称作为 url 路径 <br>
     * 仅当 value 为 "" 时生效 <br>
     * 规则为 获取方法名称 然后转换为 短横线命名法 <br>
     * 如 方法名为  getUserList 则 url 为 /user/get-user-list
     *
     * @return 标识
     */
    boolean useNameAsUrl() default true;

}
