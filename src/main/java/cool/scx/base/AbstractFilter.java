package cool.scx.base;

import cool.scx.dao.ScxDaoColumnInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * a
 *
 * @param <E> a
 * @author scx567888
 * @version 1.11.5
 */
public abstract class AbstractFilter<E extends AbstractFilter<E>> {

    /**
     * 包含的列
     */
    final Set<String> fieldNames = new HashSet<>();

    /**
     * 过滤器类型 分三种 禁用 : 0 ,包含模式 : 1 排除模式 : 2
     */
    private final FilterMode filterMode;

    /**
     * a
     *
     * @param filterMode a
     */
    protected AbstractFilter(FilterMode filterMode) {
        this.filterMode = filterMode;
    }

    /**
     * 添加 包含类型的列
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    public final E add(String... fieldNames) {
        this.fieldNames.addAll(Arrays.asList(fieldNames));
        return self();
    }

    /**
     * 根据指定名称 移除 包含类型的列
     *
     * @param fieldNames 包含的列名 (注意是 java 字段名称 ,不是 数据库 字段名称)
     * @return this 方便链式调用
     */
    public final E remove(String... fieldNames) {
        for (var fieldName : fieldNames) {
            this.fieldNames.remove(fieldName);
        }
        return self();
    }

    /**
     * 清除所有 包含类型的列
     *
     * @return this 方便链式调用
     */
    public final E clear() {
        this.fieldNames.clear();
        return self();
    }

    /**
     * 过滤
     *
     * @param scxDaoColumnInfos 带过滤的列表
     * @return 过滤后的列表
     */
    public final ScxDaoColumnInfo[] filter(ScxDaoColumnInfo... scxDaoColumnInfos) {
        return this.fieldNames.size() == 0 ? switch (this.filterMode) {
            case INCLUDED -> new ScxDaoColumnInfo[0];
            case EXCLUDED -> scxDaoColumnInfos;
        } : switch (this.filterMode) {
            case INCLUDED -> Arrays.stream(scxDaoColumnInfos).filter(c -> this.fieldNames.contains(c.fieldName())).toArray(ScxDaoColumnInfo[]::new);
            case EXCLUDED -> Arrays.stream(scxDaoColumnInfos).filter(c -> !this.fieldNames.contains(c.fieldName())).toArray(ScxDaoColumnInfo[]::new);
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
     * a
     *
     * @return a
     */
    @SuppressWarnings("unchecked")
    private E self() {
        return (E) this;
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
        EXCLUDED
    }

}
