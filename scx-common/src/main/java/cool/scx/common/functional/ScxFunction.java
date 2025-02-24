package cool.scx.common.functional;

/// 支持显式抛出异常的 Function.
///
/// @param <T> 参数类型
/// @param <R> 返回值类型
/// @param <E> 异常类型
/// @author scx567888
/// @version 0.0.1
/// @see java.util.function.Function
@FunctionalInterface
public interface ScxFunction<T, R, E extends Exception> {

    /// apply
    ///
    /// @param t 方法参数
    /// @return 方法结果
    /// @throws E 异常
    R apply(T t) throws E;

}
