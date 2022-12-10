package cool.scx.functional;

/**
 * Scx 核心 handler 有参数 有返回值 允许抛出异常 .
 *
 * @param <A> 参数类型
 * @param <R> 返回值类型
 * @param <E> 异常类型
 * @author scx567888
 * @version 0.0.1
 */
@FunctionalInterface
public interface ScxHandlerARE<A, R, E extends Exception> {

    /**
     * handle
     *
     * @param a 参数
     * @return 结果
     * @throws E 异常
     */
    R handle(A a) throws E;

}
