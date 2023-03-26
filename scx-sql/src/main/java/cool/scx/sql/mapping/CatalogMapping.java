package cool.scx.sql.mapping;

public interface CatalogMapping {

    String catalogName();

    SchemaMapping[] schemas();

}
