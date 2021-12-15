package cool.scx;

/**
 * Scx 核心 handler , 没有返回值 , 没有参数 , 不允许抛出异常 . 相当与 {@link Runnable}
 *
 * @author scx567888
 * @version 1.9.21
 */
public interface ScxHandlerV {

    /**
     * handle
     */
    void handle();

}
