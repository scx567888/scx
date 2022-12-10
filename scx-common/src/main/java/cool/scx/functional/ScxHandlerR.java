package cool.scx.functional;

/**
 * Scx 核心 handler 没有参数 有返回值 不允许抛出异常 . 相当于 {@link java.util.function.Supplier}
 *
 * @param <R> 返回值类型
 * @author scx567888
 * @version 0.0.1
 */
@FunctionalInterface
public interface ScxHandlerR<R> {

    /**
     * handle
     *
     * @return 结果
     */
    R handle();

}
