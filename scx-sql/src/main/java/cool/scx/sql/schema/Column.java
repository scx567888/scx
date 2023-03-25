package cool.scx.sql.schema;

public interface Column {

    String tableName();

    String columnName();

    String typeName();

    Integer columnSize();

    Boolean isNullable();

    Boolean isAutoincrement();

}
