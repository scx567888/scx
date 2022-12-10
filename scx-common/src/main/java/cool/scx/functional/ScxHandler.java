package cool.scx.functional;

/**
 * Scx 核心 handler 没有参数 没有返回值 不允许抛出异常 . 相当于 {@link java.lang.Runnable} <br>
 * 变体后缀说明 : <br>
 * 有参数的 添加后缀 A . <br>
 * 有返回值的 添加后缀 R . <br>
 * 有异常抛出的 添加后缀 E .
 *
 * @author scx567888
 * @version 0.0.1
 */
@FunctionalInterface
public interface ScxHandler {

    /**
     * handle
     */
    void handle();

}
