package cool.scx.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static cool.scx.common.constant.AnnotationValue.NULL;

/**
 * 从 body 获取参数
 *
 * @author scx567888
 * @version 0.5.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface FromBody {

    /**
     * 是否将整个请求体作为参数进行转换
     *
     * @return k
     */
    boolean useAllBody() default false;

    /**
     * 此值用来控制取值方式 并和 useAllBody 相关
     * 1, useAllBody 为 false 时 :
     * value 为空时 会以参数名称为标准作为 value
     * value 不为空时会先将 body 转换 map 对象 然后根据 value 进行分层获取
     * 如 userList.name , name , car.color 等
     * 会将前台发来的参数转换为 jsonTree 对象 并获取对应的节点
     * 2, useAllBody 为 true 时 :
     * value 的值则会被忽略
     * 整个 请求体会被转换成参数
     *
     * @return value
     */
    String value() default NULL;

    /**
     * 是否必填
     *
     * @return 是否必填
     */
    boolean required() default true;

}
