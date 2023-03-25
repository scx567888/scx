package cool.scx.sql.schema;

public interface Table {

    String catalog();

    String schema();

    String tableName();

    Column[] columns();

    PrimaryKey[] primaryKeys();

}
