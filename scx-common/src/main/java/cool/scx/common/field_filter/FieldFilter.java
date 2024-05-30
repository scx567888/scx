package cool.scx.common.field_filter;

import java.util.Set;

/**
 * 列过滤器
 *
 * @author scx567888
 * @version 0.1.3
 */
public interface FieldFilter {

    /**
     * 白名单模式
     *
     * @param fieldNames a
     * @return a
     */
    static FieldFilter ofIncluded(String... fieldNames) {
        return new IncludedFieldFilter().addIncluded(fieldNames);
    }

    /**
     * 黑名单模式
     *
     * @param fieldNames a
     * @return a
     */
    static FieldFilter ofExcluded(String... fieldNames) {
        return new ExcludedFieldFilter().addExcluded(fieldNames);
    }

    /**
     * 添加 白名单
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    FieldFilter addIncluded(String... fieldNames);

    /**
     * 添加 黑名单
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    FieldFilter addExcluded(String... fieldNames);

    /**
     * 移除白名单
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    FieldFilter removeIncluded(String... fieldNames);

    /**
     * 移除黑名单
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    FieldFilter removeExcluded(String... fieldNames);

    /**
     * 清除所有 包含类型的列
     *
     * @return this 方便链式调用
     */
    FieldFilter clear();

    /**
     * 获取当前模式
     *
     * @return mode 分三种 禁用 : 0 ,包含模式 : 1 排除模式 : 2
     */
    FilterMode getFilterMode();

    Set<String> getFieldNames();

    boolean getIgnoreNullValue();

    FieldFilter ignoreNullValue(boolean ignoreNullValue);

}
