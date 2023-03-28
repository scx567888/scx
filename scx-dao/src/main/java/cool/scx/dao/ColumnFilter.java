package cool.scx.dao;

import cool.scx.dao.mapping.ColumnInfo;
import cool.scx.dao.mapping.TableInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 列过滤器
 *
 * @author scx567888
 * @version 0.1.3
 */
public final class ColumnFilter {

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
    private final boolean excludeIfFieldValueIsNull;

    /**
     * a
     *
     * @param filterMode                a
     * @param excludeIfFieldValueIsNull a
     */
    private ColumnFilter(FilterMode filterMode, boolean excludeIfFieldValueIsNull) {
        this.filterMode = filterMode;
        this.excludeIfFieldValueIsNull = excludeIfFieldValueIsNull;
    }

    /**
     * 启用白名单模式 (当一个实体类所对应的 field 的值为 null 时, 会将此 field 所对应的列排除, 详情请看 {@link ColumnFilter#ofIncluded(boolean)})
     *
     * @return a
     */
    public static ColumnFilter ofIncluded() {
        return ofIncluded(true);
    }

    /**
     * 启用白名单模式
     *
     * @param excludeIfFieldValueIsNull 当一个实体类所对应的 field 的值为 null 时, 是否将此 field 所对应的列排除
     * @return a
     */
    public static ColumnFilter ofIncluded(boolean excludeIfFieldValueIsNull) {
        return new ColumnFilter(FilterMode.INCLUDED, excludeIfFieldValueIsNull);
    }

    /**
     * 白名单模式
     *
     * @param fieldNames a
     * @return a
     */
    public static ColumnFilter ofIncluded(String... fieldNames) {
        return ofIncluded().addIncluded(fieldNames);
    }

    /**
     * 白名单模式
     *
     * @param excludeIfFieldValueIsNull a
     * @param fieldNames                a
     * @return a
     */
    public static ColumnFilter ofIncluded(boolean excludeIfFieldValueIsNull, String... fieldNames) {
        return ofIncluded(excludeIfFieldValueIsNull).addIncluded(fieldNames);
    }

    /**
     * 启用黑名单模式 (当一个实体类所对应的 field 的值为 null 时, 会将此 field 所对应的列排除, 详情请看 {@link ColumnFilter#ofExcluded(boolean)})
     *
     * @return a
     */
    public static ColumnFilter ofExcluded() {
        return ofExcluded(true);
    }

    /**
     * 启动黑名单模式
     *
     * @param excludeIfFieldValueIsNull 当一个实体类所对应的 field 的值为 null 时, 是否将此 field 所对应的列排除
     * @return a
     */
    public static ColumnFilter ofExcluded(boolean excludeIfFieldValueIsNull) {
        return new ColumnFilter(FilterMode.EXCLUDED, excludeIfFieldValueIsNull);
    }

    /**
     * 黑名单模式
     *
     * @param fieldNames a
     * @return a
     */
    public static ColumnFilter ofExcluded(String... fieldNames) {
        return ofExcluded().addExcluded(fieldNames);
    }

    /**
     * 黑名单模式
     *
     * @param excludeIfFieldValueIsNull a
     * @param fieldNames                a
     * @return a
     */
    public static ColumnFilter ofExcluded(boolean excludeIfFieldValueIsNull, String... fieldNames) {
        return ofExcluded(excludeIfFieldValueIsNull).addExcluded(fieldNames);
    }

    /**
     * 添加 包含类型的列
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    private ColumnFilter _addFieldNames(String... fieldNames) {
        this.fieldNames.addAll(Arrays.asList(fieldNames));
        return this;
    }

    /**
     * 根据指定名称 移除 包含类型的列
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    private ColumnFilter _removeFieldNames(String... fieldNames) {
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
    public ColumnFilter addIncluded(String... fieldNames) {
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
    public ColumnFilter addExcluded(String... fieldNames) {
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
    public ColumnFilter removeIncluded(String... fieldNames) {
        return addExcluded(fieldNames);
    }

    /**
     * 移除黑名单
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    public ColumnFilter removeExcluded(String... fieldNames) {
        return addIncluded(fieldNames);
    }

    /**
     * 清除所有 包含类型的列
     *
     * @return this 方便链式调用
     */
    public ColumnFilter clear() {
        this.fieldNames.clear();
        return this;
    }

    /**
     * 过滤
     *
     * @param tableInfo 带过滤的列表
     * @return 过滤后的列表
     */
    public ColumnInfo[] filter(TableInfo<?> tableInfo) {
        return this.fieldNames.size() == 0 ? switch (this.filterMode) {
            case INCLUDED -> new ColumnInfo[0];
            case EXCLUDED -> tableInfo.columns();
        } : switch (this.filterMode) {
            case INCLUDED -> {
                var list = new ArrayList<ColumnInfo>();
                for (var fieldName : this.fieldNames) {
                    list.add(tableInfo.getColumn(fieldName));
                }
                yield list.toArray(ColumnInfo[]::new);
            }
            case EXCLUDED -> {
                var objects = new ArrayList<>(Arrays.asList(tableInfo.columns()));
                for (var fieldName : this.fieldNames) {
                    objects.remove(tableInfo.getColumn(fieldName));
                }
                yield objects.toArray(ColumnInfo[]::new);
            }
        };
    }

    /**
     * 获取当前模式
     *
     * @return mode 分三种 禁用 : 0 ,包含模式 : 1 排除模式 : 2
     */
    public FilterMode filterMode() {
        return filterMode;
    }

    /**
     * 过滤
     *
     * @param entity    a
     * @param tableInfo 带过滤的列表
     * @return 过滤后的列表
     */
    public ColumnInfo[] filter(Object entity, TableInfo<?> tableInfo) {
        return this.excludeIfFieldValueIsNull ? excludeIfFieldValueIsNull(entity, filter(tableInfo)) : filter(tableInfo);
    }

    /**
     * 过滤空值
     *
     * @param entity            e
     * @param scxDaoColumnInfos s
     * @return e
     */
    private ColumnInfo[] excludeIfFieldValueIsNull(Object entity, ColumnInfo... scxDaoColumnInfos) {
        return Arrays.stream(scxDaoColumnInfos).filter(field -> field.javaFieldValue(entity) != null).toArray(ColumnInfo[]::new);
    }

    /**
     * 过滤模式
     */
    public enum FilterMode {

        /**
         * 包含模式
         */
        INCLUDED,

        /**
         * 排除模式
         */
        EXCLUDED;

        /**
         * a
         *
         * @param filterModeStr a
         * @return a
         */
        public static FilterMode of(String filterModeStr) {
            return valueOf(filterModeStr.trim().toUpperCase());
        }

    }

}
