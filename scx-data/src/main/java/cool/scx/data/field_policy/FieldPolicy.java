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

    /// 清楚 fieldNames
    FieldPolicy clearFieldNames();

    /// 设置忽略空值
    FieldPolicy ignoreNullValue(boolean ignoreNullValue);

    /// 是否忽略 空值
    boolean ignoreNullValue();

    /// 设置字段表达式
    FieldPolicy fieldExpression(String fieldName, String expression);

    /// 获取字段表达式
    Map<String, String> fieldExpressions();

    /// 清除所有表达式
    FieldPolicy clearFieldExpressions();

}
