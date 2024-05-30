package cool.scx.data.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static cool.scx.common.util.AnnotationUtils.NULL;

/**
 * 添加此注解的 字段 在创建数据表是会采用 value 上的类型
 * 如果不添加 则会根据 字段的类型进行创建
 *
 * @author scx567888
 * @version 0.3.6
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * 列的名称 默认取字段名并进行 驼峰 转下划线
     *
     * @return 列的名称
     */
    String columnName() default NULL;

    /**
     * 数据库字段类型 仅用于 创建或修复表时
     *
     * @return 字段类型
     */
    DataType[] dataType() default {};

    /**
     * 数据库默认值 仅用于 创建或修复表时
     *
     * @return 数据库默认值
     */
    String defaultValue() default NULL;

    /**
     * 数据库更新时值 仅用于 创建或修复表时
     * todo 支持函数以实现自定义
     *
     * @return 数据库更新时值
     */
    String onUpdate() default NULL;

    /**
     * 是否必填 仅用于 创建或修复表时
     *
     * @return 是否必填
     */
    boolean notNull() default false;

    /**
     * 此字段是否为自增 仅用于 创建或修复表时
     *
     * @return 是否为自增
     */
    boolean autoIncrement() default false;

    /**
     * 此字段是否为主键 仅用于 创建或修复表时
     *
     * @return 是否为主键
     */
    boolean primary() default false;

    /**
     * 是否唯一 (注意 : 当 ScxContext.coreConfig().tombstone() 为 true 时) 会和 tombstone 字段 创建联合的唯一约束
     *
     * @return 是否唯一
     */
    boolean unique() default false;

    /**
     * 是否需要添加索引 仅用于 创建或修复表时
     *
     * @return 是否需要添加索引
     */
    boolean index() default false;

}
