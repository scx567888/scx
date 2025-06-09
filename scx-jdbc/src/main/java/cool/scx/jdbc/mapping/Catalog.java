package cool.scx.jdbc.mapping;

/// Catalog
///
/// @author scx567888
/// @version 0.0.1
public interface Catalog {

    String name();

    Schema[] schemas();

    default Schema getSchema(String name) {
        for (var schema : schemas()) {
            if (name.equals(schema.name())) {
                return schema;
            }
        }
        return null;
    }

}
