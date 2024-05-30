package cool.scx.jdbc.mapping;

/**
 * 索引
 */
public interface Index {

    String name();

    String columnName();

    boolean unique();

}
