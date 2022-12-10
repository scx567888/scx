package cool.scx.functional;

/**
 * Scx 核心 handler 没有参数 没有返回值 允许抛出异常 .
 *
 * @param <E> 异常类型
 * @author scx567888
 * @version 0.0.1
 */
@FunctionalInterface
public interface ScxHandlerE<E extends Exception> {

    /**
     * handle
     *
     * @throws E 异常
     */
    void handle() throws E;

}
