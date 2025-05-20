package cool.scx.data.field_policy;

/// 字段策略
///
/// @author scx567888
/// @version 0.0.1
public interface FieldPolicy<T extends FieldPolicy<T>> {

    /// 设置 包含
    T include(String... fieldNames);

    /// 设置 排除
    T exclude(String... fieldNames);

    /// 获取 当前模式
    FilterMode getFilterMode();

    /// 获取 FieldName
    String[] getFieldNames();

    /// 清除 fieldNames
    T clearFieldNames();

}
