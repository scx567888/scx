package cool.scx.sql.mapping;

public interface ColumnMapping {

    String tableName();

    String columnName();

    String typeName();

    Integer columnSize();

    Boolean isNullable();

    Boolean isAutoincrement();

    String defaultValue();

}
