package cool.scx.functional;

/**
 * 支持显式抛出异常的 Runnable.
 *
 * @param <E> 异常类型
 * @author scx567888
 * @version 0.0.1
 */
@FunctionalInterface
public interface ScxRunnable<E extends Exception> {

    /**
     * <p>run.</p>
     *
     * @throws E if any.
     */
    void run() throws E;

}
