package cool.scx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
     * 数据库字段类型 仅用于 创建或修复表时
     *
     * @return 字段类型
     */
    String type() default "";

    /**
     * 是否唯一
     *
     * @return 是否唯一
     */
    boolean unique() default false;

    /**
     * 是否必填 仅用于 创建或修复表时
     *
     * @return 是否必填
     */
    boolean notNull() default false;

    /**
     * 是否需要添加索引 仅用于 创建或修复表时
     *
     * @return 是否需要添加索引
     */
    boolean needIndex() default false;

    /**
     * 数据库默认值 仅用于 创建或修复表时
     *
     * @return 数据库默认值
     */
    String defaultValue() default "";

    /**
     * 数据库更新时值 仅用于 创建或修复表时
     *
     * @return 数据库更新时值
     */
    String onUpdateValue() default "";

    /**
     * 此字段是否为主键 仅用于 创建或修复表时
     *
     * @return 是否为主键
     */
    boolean primaryKey() default false;

    /**
     * 插入时是否过滤掉此字段 如 createTime
     *
     * @return 插入时是否过滤掉此字段
     */
    boolean excludeOnInsert() default false;

    /**
     * 更新时是否过滤掉此字段 如 id
     *
     * @return 更新时是否过滤掉此字段
     */
    boolean excludeOnUpdate() default false;

    /**
     * 此字段是否为自增 仅用于 创建或修复表时
     *
     * @return 是否为自增
     */
    boolean autoIncrement() default false;

    /**
     * 数据库 查询 时是否使用 模糊查询
     *
     * @return 是否使用 模糊查询
     */
    boolean useLike() default false;

}
