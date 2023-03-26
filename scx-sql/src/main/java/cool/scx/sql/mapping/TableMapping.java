package cool.scx.sql.mapping;

public interface TableMapping<C extends ColumnMapping, P extends PrimaryKeyMapping> {

    default String catalog() {
        return null;
    }

    default String schema() {
        return null;
    }

    String tableName();

    C[] columns();

    P[] primaryKeys();

    C getColumn(String column);

}
