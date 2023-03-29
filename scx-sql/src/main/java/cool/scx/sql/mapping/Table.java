package cool.scx.sql.mapping;

public interface Table<C extends Column> {

    default String catalog() {
        return null;
    }

    default String schema() {
        return null;
    }

    String name();

    C[] columns();

    Key[] keys();

    Index[] indexes();

    C getColumn(String column);

}
