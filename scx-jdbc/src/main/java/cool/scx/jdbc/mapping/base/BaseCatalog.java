package cool.scx.jdbc.mapping.base;

import cool.scx.jdbc.mapping.Catalog;
import cool.scx.jdbc.mapping.Schema;

import java.util.HashMap;
import java.util.Map;

/// 用于手动编写 Catalog
///
/// @author scx567888
/// @version 0.0.1
public class BaseCatalog implements Catalog {

    private final Map<String, BaseSchema> schemaMap = new HashMap<>();

    private String name;

    public BaseCatalog() {
    }

    public BaseCatalog(Catalog oldCatalog) {
        setName(oldCatalog.name());
        for (var schema : oldCatalog.schemas()) {
            addSchema(schema);
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public BaseSchema[] schemas() {
        return schemaMap.values().toArray(BaseSchema[]::new);
    }

    @Override
    public BaseSchema getSchema(String name) {
        return schemaMap.get(name);
    }

    public BaseCatalog setName(String name) {
        this.name = name;
        return this;
    }

    public BaseCatalog addSchema(Schema oldSchema) {
        var schema = new BaseSchema(oldSchema);
        schemaMap.put(schema.name(), schema);
        return this;
    }

    public BaseCatalog removeSchema(String name) {
        schemaMap.remove(name);
        return this;
    }

    public BaseCatalog clearSchemas() {
        schemaMap.clear();
        return this;
    }

}
