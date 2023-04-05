package cool.scx.data.jdbc.mapping;

public interface Key {

    /**
     * key 名称
     *
     * @return a
     */
    String name();

    /**
     * 对应列名称
     *
     * @return a
     */
    String columnName();

    /**
     * 是否为主键
     *
     * @return a
     */
    boolean primaryKey();

}
