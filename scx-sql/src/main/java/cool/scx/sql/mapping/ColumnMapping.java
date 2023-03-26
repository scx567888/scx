package cool.scx.sql.mapping;

public interface ColumnMapping {

    String tableName();

    String columnName();

    String typeName();

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
     * 唯一
     *
     * @return 是否唯一
     */
    boolean unique();

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

}
