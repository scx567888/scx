package cool.scx.functional;

/**
 * Scx 核心 handler , 有返回值 , 不允许抛出异常 .
 *
 * @param <A> 参数类型
 * @param <R> 返回值类型
 * @author scx567888
 * @version 1.7.3
 */
public interface ScxHandlerR<A, R> {

    /**
     * handle
     *
     * @param a 参数
     * @return 异常
     */
    R handle(A a);

}
