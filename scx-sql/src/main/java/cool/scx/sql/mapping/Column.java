package cool.scx.sql.mapping;

public interface Column {

    /**
     * 所属表名
     *
     * @return 表名
     */
    default String table() {
        return null;
    }

    /**
     * 列名
     *
     * @return 列名
     */
    String name();

    /**
     * 类型名称
     *
     * @return 类型名称
     */
    String typeName();

    /**
     * 列大小
     *
     * @return 列大小
     */
    Integer columnSize();

    /**
     * 非 Null
     *
     * @return 是否 非 Null
     */
    boolean notNull();

    /**
     * 自动递增
     *
     * @return 是否 自动递增
     */
    boolean autoIncrement();

    /**
     * 默认值
     *
     * @return 默认值
     */
    String defaultValue();

    /**
     * 更新时
     *
     * @return 更新时
     */
    String onUpdateValue();

    /**
     * 唯一
     *
     * @return 是否唯一
     */
    boolean unique();

    /**
     * 是否是主键
     *
     * @return r
     */
    boolean primaryKey();

}
