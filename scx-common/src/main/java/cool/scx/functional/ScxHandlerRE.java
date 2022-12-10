package cool.scx.functional;

/**
 * Scx 核心 handler 没有参数 有返回值 允许抛出异常 . 相当于 {@link java.util.concurrent.Callable}
 *
 * @param <R> 返回值类型
 * @param <E> 异常类型
 * @author scx567888
 * @version 0.0.1
 */
@FunctionalInterface
public interface ScxHandlerRE<R, E extends Exception> {

    /**
     * handle
     *
     * @return 结果
     * @throws E 异常
     */
    R handle() throws E;

}
