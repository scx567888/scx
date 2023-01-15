package cool.scx.functional;

/**
 * 支持显式抛出异常的 Function.
 *
 * @param <A> 参数类型
 * @param <R> 返回值类型
 * @param <E> 异常类型
 * @author scx567888
 * @version 0.0.1
 */
@FunctionalInterface
public interface ScxFunction<A, R, E extends Exception> {

    R apply(A a) throws E;

}
