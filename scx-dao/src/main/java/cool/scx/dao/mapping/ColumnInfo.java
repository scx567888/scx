package cool.scx.dao.mapping;

import cool.scx.sql.mapping.ColumnMapping;

import java.lang.reflect.Field;

/**
 * <p>ColumnInfo.</p>
 *
 * @author scx567888
 * @version 0.1.3
 */
public interface ColumnInfo extends ColumnMapping {

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

    default boolean primaryKey() {
        return false;
    }

    /**
     * 用户指定的类型
     */
    default String type() {
        return "VARCHAR(128)";
    }

    default boolean needIndex() {
        return false;
    }

}
