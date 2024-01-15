package cool.scx.jdbc.mapping;

public interface Schema {

    String catalog();

    String name();

    Table<?>[] tables();

}
