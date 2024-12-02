package cool.scx.data.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static cool.scx.common.constant.AnnotationValue.NULL;

/**
 * ScxModel
 * model 层映射
 * 设置此注解的方法 必须同时 继承 BaseModel
 *
 * @author scx567888
 * @version 0.0.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * 表名称
     *
     * @return 表全限定名称
     */
    String tableName() default NULL;

    /**
     * 表名称前缀
     *
     * @return 表前缀
     */
    String tablePrefix() default NULL;

}
