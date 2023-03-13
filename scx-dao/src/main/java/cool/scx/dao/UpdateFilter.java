package cool.scx.dao;

import cool.scx.sql.mapping.ColumnInfo;
import cool.scx.sql.mapping.TableInfo;

import java.util.Arrays;

/**
 * 更新及保存数据是的字段过滤器 (注意 excludeIfFieldValueIsNull 在 批量保存时无效)
 *
 * @author scx567888
 * @version 0.1.3
 */
public final class UpdateFilter extends ColumnInfoFilter<UpdateFilter> {

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
    private UpdateFilter(FilterMode filterMode, boolean excludeIfFieldValueIsNull) {
        super(filterMode);
        this.excludeIfFieldValueIsNull = excludeIfFieldValueIsNull;
    }

    /**
     * 启用白名单模式 (当一个实体类所对应的 field 的值为 null 时, 会将此 field 所对应的列排除, 详情请看 {@link UpdateFilter#ofIncluded(boolean)})
     *
     * @return a
     */
    public static UpdateFilter ofIncluded() {
        return ofIncluded(true);
    }

    /**
     * 启用黑名单模式 (当一个实体类所对应的 field 的值为 null 时, 会将此 field 所对应的列排除, 详情请看 {@link UpdateFilter#ofExcluded(boolean)})
     *
     * @return a
     */
    public static UpdateFilter ofExcluded() {
        return ofExcluded(true);
    }

    /**
     * 启用白名单模式
     *
     * @param excludeIfFieldValueIsNull 当一个实体类所对应的 field 的值为 null 时, 是否将此 field 所对应的列排除
     * @return a
     */
    public static UpdateFilter ofIncluded(boolean excludeIfFieldValueIsNull) {
        return new UpdateFilter(FilterMode.INCLUDED, excludeIfFieldValueIsNull);
    }

    /**
     * 启动黑名单模式
     *
     * @param excludeIfFieldValueIsNull 当一个实体类所对应的 field 的值为 null 时, 是否将此 field 所对应的列排除
     * @return a
     */
    public static UpdateFilter ofExcluded(boolean excludeIfFieldValueIsNull) {
        return new UpdateFilter(FilterMode.EXCLUDED, excludeIfFieldValueIsNull);
    }

    /**
     * 白名单模式
     *
     * @param fieldNames a
     * @return a
     */
    public static UpdateFilter ofIncluded(String... fieldNames) {
        return ofIncluded().addIncluded(fieldNames);
    }

    /**
     * 黑名单模式
     *
     * @param fieldNames a
     * @return a
     */
    public static UpdateFilter ofExcluded(String... fieldNames) {
        return ofExcluded().addExcluded(fieldNames);
    }

    /**
     * 白名单模式
     *
     * @param excludeIfFieldValueIsNull a
     * @param fieldNames                a
     * @return a
     */
    public static UpdateFilter ofIncluded(boolean excludeIfFieldValueIsNull, String... fieldNames) {
        return ofIncluded(excludeIfFieldValueIsNull).addIncluded(fieldNames);
    }

    /**
     * 黑名单模式
     *
     * @param excludeIfFieldValueIsNull a
     * @param fieldNames                a
     * @return a
     */
    public static UpdateFilter ofExcluded(boolean excludeIfFieldValueIsNull, String... fieldNames) {
        return ofExcluded(excludeIfFieldValueIsNull).addExcluded(fieldNames);
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

    @Deprecated
    public ColumnInfo[] filter(Object entity, ColumnInfo... scxDaoColumnInfos) {
        return this.excludeIfFieldValueIsNull ? excludeIfFieldValueIsNull(entity, filter(scxDaoColumnInfos)) : filter(scxDaoColumnInfos);
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

}
