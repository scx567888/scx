package cool.scx.functional;

/**
 * 支持显式抛出异常的 Consumer.
 *
 * @param <A> 参数类型
 * @param <E> 异常类型
 * @author scx567888
 * @version 0.0.1
 */
@FunctionalInterface
public interface ScxConsumer<A, E extends Exception> {

    /**
     * <p>accept.</p>
     *
     * @param a a A object
     * @throws E if any.
     */
    void accept(A a) throws E;

}
