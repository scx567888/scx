package cool.scx.data.jdbc.mapping;

import cool.scx.jdbc.mapping.Table;

public interface EntityTable<Entity> extends Table {

    Class<Entity> entityClass();

    @Override
    EntityColumn[] columns();

    @Override
    EntityColumn getColumn(String name);

}
