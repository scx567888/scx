package cool.scx;

/**
 * Scx 核心 handler , 没有参数 , 有返回值 , 不允许抛出异常 .
 *
 * @param <R> 返回值类型
 * @author scx567888
 * @version 1.9.21
 */
@FunctionalInterface
public interface ScxHandlerVR<R> {

    /**
     * handle
     *
     * @return 异常
     */
    R handle();

}
