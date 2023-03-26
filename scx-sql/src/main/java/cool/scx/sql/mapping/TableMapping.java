package cool.scx.sql.mapping;

public interface TableMapping {

    String catalog();

    String schema();

    String tableName();

    ColumnMapping[] columns();

    PrimaryKeyMapping[] primaryKeys();

    ColumnMapping getColumn(String column);

}
