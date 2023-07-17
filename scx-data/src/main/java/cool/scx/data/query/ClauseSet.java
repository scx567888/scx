package cool.scx.data.query;

/**
 * 从句集合
 *
 * @param <T> 类型
 */
public interface ClauseSet<T> {

    /**
     * 获取从句集合
     *
     * @return a
     */
    T[] clauses();

}
