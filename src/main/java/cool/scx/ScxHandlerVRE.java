package cool.scx;

/**
 * Scx 核心 handler , 没有参数 , 有返回值 , 允许抛出异常 . 相当与 {@link java.util.concurrent.Callable}
 *
 * @param <R> 返回值类型
 * @param <E> 异常类型
 * @author scx567888
 * @version 1.9.7
 */
public interface ScxHandlerVRE<R, E extends Exception> {

    /**
     * handle
     *
     * @return 结果
     * @throws E 异常
     */
    R handle() throws E;

}
