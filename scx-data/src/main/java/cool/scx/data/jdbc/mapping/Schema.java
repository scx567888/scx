package cool.scx.data.jdbc.mapping;

public interface Schema {

    String catalog();

    String name();

    Table<?>[] tables();

}
