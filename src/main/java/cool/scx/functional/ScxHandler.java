package cool.scx.functional;

/**
 * Scx 核心 handler , 没有返回值 , 不允许抛出异常 .
 * <br>
 * 变体后缀说明 :
 * <br>
 * 没有参数的 添加后缀 V .
 * <br>
 * 有返回值的 添加后缀 R .
 * <br>
 * 有异常抛出的 添加后缀 E .
 *
 * @param <A> 参数类型
 * @author scx567888
 * @version 1.7.3
 */
@FunctionalInterface
public interface ScxHandler<A> {

    /**
     * handle
     *
     * @param a 参数
     */
    void handle(A a);

}
