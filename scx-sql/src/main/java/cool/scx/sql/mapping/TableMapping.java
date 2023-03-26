package cool.scx.sql.mapping;

public interface TableMapping<C extends ColumnMapping, P extends PrimaryKeyMapping> {

    String catalog();

    String schema();

    String tableName();

    C[] columns();

    P[] primaryKeys();

    C getColumn(String column);

}
