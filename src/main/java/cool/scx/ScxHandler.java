package cool.scx;

/**
 * Scx 核心 handler , 没有返回值 不允许抛出异常
 *
 * @param <A> 参数类型
 * @author scx567888
 * @version 1.7.3
 */
public interface ScxHandler<A> {

    /**
     * handle
     *
     * @param a 参数
     */
    void handle(A a);

}
