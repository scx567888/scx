package cool.scx.base;

import cool.scx.dao.ScxDaoColumnInfo;

import java.util.Arrays;

/**
 * 更新及保存数据是的字段过滤器 (注意 excludeIfFieldValueIsNull 在 批量保存时无效)
 */
public final class UpdateFilter extends AbstractFilter<UpdateFilter> {

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
     * a
     *
     * @return a
     */
    public static UpdateFilter ofIncluded() {
        return ofIncluded(true);
    }

    /**
     * a
     *
     * @return a
     */
    public static UpdateFilter ofExcluded() {
        return ofExcluded(true);
    }

    /**
     * a
     *
     * @param excludeIfFieldValueIsNull a
     * @return a
     */
    public static UpdateFilter ofIncluded(boolean excludeIfFieldValueIsNull) {
        return new UpdateFilter(FilterMode.INCLUDED, excludeIfFieldValueIsNull);
    }

    /**
     * a
     *
     * @param excludeIfFieldValueIsNull a
     * @return a
     */
    public static UpdateFilter ofExcluded(boolean excludeIfFieldValueIsNull) {
        return new UpdateFilter(FilterMode.EXCLUDED, excludeIfFieldValueIsNull);
    }

    /**
     * 过滤
     *
     * @param entity            a
     * @param scxDaoColumnInfos 带过滤的列表
     * @return 过滤后的列表
     */
    public ScxDaoColumnInfo[] filter(Object entity, ScxDaoColumnInfo... scxDaoColumnInfos) {
        return this.excludeIfFieldValueIsNull ? excludeIfFieldValueIsNull(entity, filter(scxDaoColumnInfos)) : filter(scxDaoColumnInfos);
    }

    /**
     * 过滤空值
     *
     * @param entity            e
     * @param scxDaoColumnInfos s
     * @return e
     */
    private ScxDaoColumnInfo[] excludeIfFieldValueIsNull(Object entity, ScxDaoColumnInfo... scxDaoColumnInfos) {
        return Arrays.stream(scxDaoColumnInfos).filter(field -> field.getFieldValue(entity) != null).toArray(ScxDaoColumnInfo[]::new);
    }

}
