package cool.scx.data.query;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static cool.scx.data.query.FieldFilterOption.EXCLUDE_IF_FIELD_VALUE_IS_NULL;
import static cool.scx.data.query.FilterMode.EXCLUDED;

/**
 * 列过滤器
 *
 * @author scx567888
 * @version 0.1.3
 */
public final class FieldFilter {

    /**
     * 包含的列
     */
    private final Set<String> fieldNames = new HashSet<>();

    /**
     * 过滤器类型 分三种 禁用 : 0 ,包含模式 : 1 排除模式 : 2
     */
    private final FilterMode filterMode;

    /**
     * Info
     */
    private final FieldFilterOption.Info info;

    /**
     * a
     *
     * @param filterMode a
     * @param options    a
     */
    private FieldFilter(FilterMode filterMode, FieldFilterOption... options) {
        this.filterMode = filterMode;
        this.info = new FieldFilterOption.Info(options);
    }

    /**
     * 启用白名单模式 (当一个实体类所对应的 field 的值为 null 时, 会将此 field 所对应的列排除, 详情请看 {@link FieldFilter#ofIncluded(FieldFilterOption...)})
     *
     * @return a
     */
    public static FieldFilter ofIncluded() {
        return ofIncluded(EXCLUDE_IF_FIELD_VALUE_IS_NULL);
    }

    /**
     * 启用白名单模式
     *
     * @param options options
     * @return a
     */
    public static FieldFilter ofIncluded(FieldFilterOption... options) {
        return new FieldFilter(FilterMode.INCLUDED, options);
    }

    /**
     * 白名单模式
     *
     * @param fieldNames a
     * @return a
     */
    public static FieldFilter ofIncluded(String... fieldNames) {
        return ofIncluded().addIncluded(fieldNames);
    }

    /**
     * 白名单模式
     *
     * @param options    a
     * @param fieldNames a
     * @return a
     */
    public static FieldFilter ofIncluded(FieldFilterOption[] options, String... fieldNames) {
        return ofIncluded(options).addIncluded(fieldNames);
    }

    /**
     * 启用黑名单模式 (当一个实体类所对应的 field 的值为 null 时, 会将此 field 所对应的列排除, 详情请看 {@link FieldFilter#ofExcluded(FieldFilterOption...)})
     *
     * @return a
     */
    public static FieldFilter ofExcluded() {
        return ofExcluded(EXCLUDE_IF_FIELD_VALUE_IS_NULL);
    }

    /**
     * 启动黑名单模式
     *
     * @param options 当一个实体类所对应的 field 的值为 null 时, 是否将此 field 所对应的列排除
     * @return a
     */
    public static FieldFilter ofExcluded(FieldFilterOption... options) {
        return new FieldFilter(EXCLUDED, options);
    }

    /**
     * 黑名单模式
     *
     * @param fieldNames a
     * @return a
     */
    public static FieldFilter ofExcluded(String... fieldNames) {
        return ofExcluded().addExcluded(fieldNames);
    }

    /**
     * 黑名单模式
     *
     * @param options    a
     * @param fieldNames a
     * @return a
     */
    public static FieldFilter ofExcluded(FieldFilterOption[] options, String... fieldNames) {
        return ofExcluded(options).addExcluded(fieldNames);
    }

    /**
     * 添加 包含类型的列
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    private FieldFilter _addFieldNames(String... fieldNames) {
        this.fieldNames.addAll(Arrays.asList(fieldNames));
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
    public FilterMode filterMode() {
        return filterMode;
    }


    public Set<String> fieldNames() {
        return fieldNames;
    }

    public FieldFilterOption.Info info() {
        return info;
    }

}
