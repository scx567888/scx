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

    default Key[] keys(){
        return new Key[]{};
    }

    default Index[] indexes(){
        return new Index[]{};
    }

    C getColumn(String column);

}
