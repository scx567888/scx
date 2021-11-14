package cool.scx;

/**
 * Scx 核心 handler , 没有返回值 允许抛出异常
 *
 * @param <A> 参数类型
 * @param <E> 异常类型
 * @author scx567888
 * @version 1.7.3
 */
public interface ScxHandlerE<A, E extends Exception> {

    /**
     * handle
     *
     * @param a 参数
     * @throws E 异常
     */
    void handle(A a) throws E;

}
