package cool.scx.data.field_policy;

import java.util.Map;

/// 更新字段策略
public interface UpdateFieldPolicy extends FieldPolicy {

    /// 设置 全局 忽略空值
    UpdateFieldPolicy ignoreNull(boolean ignoreNull);

    /// 设置 忽略空值
    UpdateFieldPolicy ignoreNull(String fieldName, boolean ignoreNull);

    /// 设置 字段表达式 (用于插入和更新)
    UpdateFieldPolicy expressions(Expression... expressions);

    /// 获取 全局是否忽略 空值
    boolean getIgnoreNull();

    /// 获取 忽略 空值
    Map<String, Boolean> getIgnoreNulls();

    /// 获取 字段表达式
    Expression[] getExpressions();

    /// 清除 所有忽略空值
    UpdateFieldPolicy clearIgnoreNulls();

    /// 清除 所有表达式
    UpdateFieldPolicy clearExpressions();

    /// 移除 是否忽略 空值
    UpdateFieldPolicy removeIgnoreNull(String fieldName);

    /// 追加 表达式
    UpdateFieldPolicy expression(String fieldName, String expression);

    // 重写 链式调用返回值类型
    @Override
    UpdateFieldPolicy include(String... fieldNames);

    @Override
    UpdateFieldPolicy exclude(String... fieldNames);

    @Override
    UpdateFieldPolicy clearFieldNames();

}
