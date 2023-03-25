package cool.scx.sql.schema;

public interface Schema {

    String catalog();

    String schemaName();

    Table[] tables();

}
