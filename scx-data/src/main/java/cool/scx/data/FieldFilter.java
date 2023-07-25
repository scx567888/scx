package cool.scx.data;

import cool.scx.data.field_filter.FilterMode;

import java.util.HashSet;
import java.util.Set;

import static cool.scx.data.field_filter.FilterMode.EXCLUDED;
import static cool.scx.data.field_filter.FilterMode.INCLUDED;
import static java.util.Collections.addAll;

/**
 * 列过滤器
 *
 * @author scx567888
 * @version 0.1.3
 */
public class FieldFilter {

    /**
     * 包含的列
     */
    private final Set<String> fieldNames = new HashSet<>();

    /**
     * 过滤器类型 分三种 禁用 : 0 ,包含模式 : 1 排除模式 : 2
     */
    private final FilterMode filterMode;

    /**
     * 当一个实体类所对应的 field 的值为 null 时, 是否将此 field 所对应的列排除 默认启用
     */
    private boolean excludeIfFieldValueIsNull;

    private FieldFilter(FilterMode filterMode) {
        this.filterMode = filterMode;
        this.excludeIfFieldValueIsNull = true;
    }

    /**
     * 白名单模式
     *
     * @param fieldNames a
     * @return a
     */
    public static FieldFilter ofIncluded(String... fieldNames) {
        return new FieldFilter(INCLUDED).addIncluded(fieldNames);
    }

    /**
     * 黑名单模式
     *
     * @param fieldNames a
     * @return a
     */
    public static FieldFilter ofExcluded(String... fieldNames) {
        return new FieldFilter(EXCLUDED).addExcluded(fieldNames);
    }

    /**
     * 添加 包含类型的列
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    private FieldFilter _addFieldNames(String... fieldNames) {
        addAll(this.fieldNames, fieldNames);
        return this;
    }

    /**
     * 根据指定名称 移除 包含类型的列
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    private FieldFilter _removeFieldNames(String... fieldNames) {
        for (var fieldName : fieldNames) {
            this.fieldNames.remove(fieldName);
        }
        return this;
    }

    /**
     * 添加 白名单
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    public FieldFilter addIncluded(String... fieldNames) {
        return switch (filterMode) {
            case INCLUDED -> _addFieldNames(fieldNames);
            case EXCLUDED -> _removeFieldNames(fieldNames);
        };
    }

    /**
     * 添加 黑名单
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    public FieldFilter addExcluded(String... fieldNames) {
        return switch (filterMode) {
            case EXCLUDED -> _addFieldNames(fieldNames);
            case INCLUDED -> _removeFieldNames(fieldNames);
        };
    }

    /**
     * 移除白名单
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    public FieldFilter removeIncluded(String... fieldNames) {
        return addExcluded(fieldNames);
    }

    /**
     * 移除黑名单
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    public FieldFilter removeExcluded(String... fieldNames) {
        return addIncluded(fieldNames);
    }

    /**
     * 清除所有 包含类型的列
     *
     * @return this 方便链式调用
     */
    public FieldFilter clear() {
        this.fieldNames.clear();
        return this;
    }

    /**
     * 获取当前模式
     *
     * @return mode 分三种 禁用 : 0 ,包含模式 : 1 排除模式 : 2
     */
    public FilterMode getFilterMode() {
        return filterMode;
    }

    public Set<String> getFieldNames() {
        return fieldNames;
    }

    public boolean getExcludeIfFieldValueIsNull() {
        return excludeIfFieldValueIsNull;
    }

    public FieldFilter excludeIfFieldValueIsNull(boolean excludeIfFieldValueIsNull) {
        this.excludeIfFieldValueIsNull = excludeIfFieldValueIsNull;
        return this;
    }

}
