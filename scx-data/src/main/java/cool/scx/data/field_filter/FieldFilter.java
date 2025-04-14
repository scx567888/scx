package cool.scx.data.field_filter;

/// 字段过滤器
///
/// @author scx567888
/// @version 0.0.1
public interface FieldFilter {

    /// 添加 白名单
    ///
    /// @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
    /// @return this 方便链式调用
    FieldFilter addIncluded(String... fieldNames);

    /// 添加 黑名单
    ///
    /// @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
    /// @return this 方便链式调用
    FieldFilter addExcluded(String... fieldNames);

    /// 移除白名单
    ///
    /// @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
    /// @return this 方便链式调用
    FieldFilter removeIncluded(String... fieldNames);

    /// 移除黑名单
    ///
    /// @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
    /// @return this 方便链式调用
    FieldFilter removeExcluded(String... fieldNames);

    /// 设置忽略空值
    ///
    /// @param ignoreNullValue a
    /// @return a
    FieldFilter ignoreNullValue(boolean ignoreNullValue);

    /// 获取当前模式
    ///
    /// @return mode 分三种 禁用 : 0 ,包含模式 : 1 排除模式 : 2
    FilterMode getFilterMode();

    /// 获取 FieldName
    String[] getFieldNames();

    /// 忽略 空值
    boolean getIgnoreNullValue();

    /// 清除所有 包含类型的列
    FieldFilter clear();
    
    /// 添加虚拟列支持
    FieldFilter addVirtualField(String virtualFiledName, String expression);

    /// 获取虚拟列
    VirtualField[] getVirtualFields();

}
