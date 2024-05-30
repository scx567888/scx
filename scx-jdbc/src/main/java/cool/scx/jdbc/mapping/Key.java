package cool.scx.jdbc.mapping;

/**
 * 键
 */
public interface Key {

    String name();

    String columnName();

    boolean primary();

}
