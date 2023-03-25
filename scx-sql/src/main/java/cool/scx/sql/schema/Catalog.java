package cool.scx.sql.schema;

public interface Catalog {

    String catalogName();

    Schema[] schemas();

}
