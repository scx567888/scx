package cool.scx.orm.jdbc.mapping;

public interface Schema {

    String catalog();

    String name();

    Table<?>[] tables();

}
