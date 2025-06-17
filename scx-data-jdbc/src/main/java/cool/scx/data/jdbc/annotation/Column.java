package cool.scx.data.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static cool.scx.common.constant.AnnotationValue.NULL;

/// 添加此注解的 字段 在创建数据表是会采用 value 上的类型
/// 如果不添加 则会根据 字段的类型进行创建
///
/// @author scx567888
/// @version 0.0.1
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /// 用于标注字段对应的数据库列名.
    ///
    /// 如果未显式指定列名 (即 `columnName` 为空), 将默认使用字段名作为列名.
    ///
    /// 本注解不会进行任何隐式命名转换, 例如 `fileMD5` 不会自动转换为 `file_md5` 或 `file_m_d5`.
    ///
    /// 这种设计经过明确考量, 旨在保持命名一致性, 减少认知负担, 并避免因自动转换导致的歧义或错误.
    ///
    /// @return 数据库列名, 默认为字段名
    String columnName() default NULL;

    /// 数据库字段类型 仅用于 创建或修复表时
    ///
    /// @return 字段类型
    DataType[] dataType() default {};

    /// 数据库默认值 仅用于 创建或修复表时
    ///
    /// @return 数据库默认值
    String defaultValue() default NULL;

    /// 数据库更新时值 仅用于 创建或修复表时
    /// todo 支持函数以实现自定义
    ///
    /// @return 数据库更新时值
    String onUpdate() default NULL;

    /// 是否必填 仅用于 创建或修复表时
    ///
    /// @return 是否必填
    boolean notNull() default false;

    /// 此字段是否为自增 仅用于 创建或修复表时
    ///
    /// @return 是否为自增
    boolean autoIncrement() default false;

    /// 此字段是否为主键 仅用于 创建或修复表时
    ///
    /// @return 是否为主键
    boolean primary() default false;

    /// 是否唯一
    ///
    /// @return 是否唯一
    boolean unique() default false;

    /// 是否需要添加索引 仅用于 创建或修复表时
    ///
    /// @return 是否需要添加索引
    boolean index() default false;

}
