package cool.scx.dao.mapping;

import java.lang.reflect.Field;

/**
 * <p>ColumnInfo.</p>
 *
 * @author scx567888
 * @version 0.1.3
 */
public interface ColumnInfo {

    Field javaField();

    default String javaFieldName() {
        return this.javaField().getName();
    }

    default Object javaFieldValue(Object target) {
        try {
            return this.javaField().get(target);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 列名称 (数据库中的列名称)
     */
    default String columnName() {
        return javaFieldName();
    }

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
