package cool.scx.jdbc.mapping;

public interface Index {

    /**
     * 索引名称
     *
     * @return index
     */
    String name();

    /**
     * 对应列名称
     *
     * @return a
     */
    String columnName();

    /**
     * 是否唯一值
     *
     * @return a
     */
    boolean unique();

}
