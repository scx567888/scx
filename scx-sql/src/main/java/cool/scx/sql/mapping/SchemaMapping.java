package cool.scx.sql.mapping;

public interface SchemaMapping {

    String catalog();

    String schemaName();

    TableMapping[] tables();

}
