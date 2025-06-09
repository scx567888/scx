package cool.scx.jdbc.mapping;

/// Schema
///
/// @author scx567888
/// @version 0.0.1
public interface Schema {

    default String catalog() {
        return null;
    }

    String name();

    Table[] tables();

    default Table getTable(String name) {
        for (var table : tables()) {
            if (name.equals(table.name())) {
                return table;
            }
        }
        return null;
    }

}
