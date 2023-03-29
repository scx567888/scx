package cool.scx.sql.mapping;

public interface Schema {

    String catalog();

    String name();

    Table[] tables();

}
