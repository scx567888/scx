package cool.scx.data.field_policy;

/// 字段策略
///
/// @author scx567888
/// @version 0.0.1
public interface FieldPolicy {

    /// 添加 白名单
    ///
    /// @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
    /// @return this 方便链式调用
    FieldPolicy addIncluded(String... fieldNames);

    /// 添加 黑名单
    ///
    /// @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
    /// @return this 方便链式调用
    FieldPolicy addExcluded(String... fieldNames);

    /// 移除白名单
    ///
    /// @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
    /// @return this 方便链式调用
    FieldPolicy removeIncluded(String... fieldNames);

    /// 移除黑名单
    ///
    /// @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
    /// @return this 方便链式调用
    FieldPolicy removeExcluded(String... fieldNames);

    /// 设置忽略空值
    ///
    /// @param ignoreNullValue a
    /// @return a
    FieldPolicy ignoreNullValue(boolean ignoreNullValue);

    /// 获取当前模式
    ///
    /// @return mode 分三种 禁用 : 0 ,包含模式 : 1 排除模式 : 2
    FilterMode getFilterMode();

    /// 获取 FieldName
    String[] getFieldNames();

    /// 忽略 空值
    boolean getIgnoreNullValue();

    /// 清除所有 包含类型的列
    FieldPolicy clear();

    /// 添加字段表达式支持
    FieldPolicy addFieldExpression(FieldExpression... fieldExpressions);

    default FieldPolicy addFieldExpression(String fieldName, String expression) {
        return addFieldExpression(new FieldExpression(fieldName, expression));
    }

    /// 获取字段表达式
    FieldExpression[] getFieldExpressions();

}
