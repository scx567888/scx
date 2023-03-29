package cool.scx.sql.mapping;

public interface Table {

    default String catalog() {
        return null;
    }

    default String schema() {
        return null;
    }

    String name();

    Column[] columns();

    Key[] keys();

    Index[] indexes();

    Column getColumn(String column);

}
