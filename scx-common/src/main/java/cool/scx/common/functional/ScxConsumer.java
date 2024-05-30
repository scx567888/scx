package cool.scx.common.functional;

/**
 * 支持显式抛出异常的 Consumer.
 *
 * @param <T> 参数类型
 * @param <E> 异常类型
 * @author scx567888
 * @version 0.0.1
 * @see java.util.function.Consumer
 */
@FunctionalInterface
public interface ScxConsumer<T, E extends Exception> {

    /**
     * accept
     *
     * @param t 方法参数
     * @throws E 异常
     */
    void accept(T t) throws E;

}
