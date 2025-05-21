package cool.scx.data.field_policy;

import java.util.Map;

/// 查询字段策略
public interface QueryFieldPolicy extends FieldPolicy<QueryFieldPolicy> {

    /// 设置 虚拟列
    QueryFieldPolicy virtualFields(VirtualField... virtualFields);

    /// 获取 虚拟列
    VirtualField[] getVirtualFields();

    /// 清除 所有虚拟列
    QueryFieldPolicy clearVirtualFields();
    
    /// 追加 虚拟列 (用于查询)
    QueryFieldPolicy virtualField(String expression, String virtualFieldName);

    /// 追加 虚拟列 (用于查询)
    QueryFieldPolicy virtualField(String expression);

}
