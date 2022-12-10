package cool.scx.functional;

/**
 * Scx 核心 handler 有参数 没有返回值 不允许抛出异常 . 相当于 {@link java.util.function.Consumer}
 *
 * @param <A> 参数类型
 * @author scx567888
 * @version 0.0.1
 */
@FunctionalInterface
public interface ScxHandlerA<A> {

    /**
     * handle
     *
     * @param a 参数
     */
    void handle(A a);

}
