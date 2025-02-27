package cool.scx.jdbc.mapping;

/// 表
///
/// @author scx567888
/// @version 0.0.1
public interface Table {

    default String catalog() {
        return null;
    }

    default String schema() {
        return null;
    }

    String name();

    Column[] columns();

    default Key[] keys() {
        return new Key[]{};
    }

    default Index[] indexes() {
        return new Index[]{};
    }

    default Column getColumn(String name) {
        for (var column : columns()) {
            if (name.equals(column.name())) {
                return column;
            }
        }
        return null;
    }

    default Key getKey(String name) {
        for (var key : keys()) {
            if (name.equals(key.name())) {
                return key;
            }
        }
        return null;
    }

    default Index getIndex(String name) {
        for (var index : indexes()) {
            if (name.equals(index.name())) {
                return index;
            }
        }
        return null;
    }

}
