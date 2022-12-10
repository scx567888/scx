package cool.scx.functional;

/**
 * Scx 核心 handler 有参数 有返回值 不允许抛出异常 . 相当于 {@link java.util.function.Function}
 *
 * @param <A> 参数类型
 * @param <R> 返回值类型
 * @author scx567888
 * @version 0.0.1
 */
@FunctionalInterface
public interface ScxHandlerAR<A, R> {

    /**
     * handle
     *
     * @param a 参数
     * @return 结果
     */
    R handle(A a);

}
