package cool.scx.sql;

/**
 * <p>ColumnInfo.</p>
 *
 * @author scx567888
 * @version 0.1.3
 */
public interface ColumnInfo {

    /**
     * 列名称 (数据库中的列名称)
     */
    String columnName();

    default boolean notNull() {
        return false;
    }

    default boolean primaryKey() {
        return false;
    }

    default boolean autoIncrement() {
        return false;
    }

    default String defaultValue() {
        return null;
    }

    default String onUpdateValue() {
        return null;
    }

    /**
     * 类型  (数据库中的类型 , 目前仅在建表时使用)
     */
    default String type() {
        return "VARCHAR(128)";
    }

    default boolean unique() {
        return false;
    }

    default boolean needIndex() {
        return false;
    }

}
