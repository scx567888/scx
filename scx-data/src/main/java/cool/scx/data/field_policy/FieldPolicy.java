package cool.scx.data.field_policy;

import java.util.Map;

/// 字段策略
///
/// @author scx567888
/// @version 0.0.1
public interface FieldPolicy {

    /// 包含
    FieldPolicy included(String... fieldNames);

    /// 排除
    FieldPolicy excluded(String... fieldNames);

    /// 获取当前模式
    FilterMode filterMode();

    /// 获取 FieldName
    String[] fieldNames();

    /// 清除 fieldNames
    FieldPolicy clearFieldNames();

    /// 全局设置忽略空值
    FieldPolicy ignoreNull(boolean ignoreNull);

    /// 全局是否忽略 空值
    boolean ignoreNull();

    /// 设置忽略空值
    FieldPolicy ignoreNull(String fieldName, boolean ignoreNull);

    /// 移除是否忽略 空值
    FieldPolicy removeIgnoreNull(String fieldName);

    /// 忽略 空值
    Map<String, Boolean> ignoreNulls();

    /// 清除 所有忽略空值
    FieldPolicy clearIgnoreNulls();

    /// 设置字段表达式
    FieldPolicy fieldExpression(String fieldName, String expression);

    /// 字段表达式
    Map<String, String> fieldExpressions();

    /// 清除 某个表达式
    FieldPolicy removeFieldExpression(String fieldName);

    /// 清除 所有表达式
    FieldPolicy clearFieldExpressions();

}
