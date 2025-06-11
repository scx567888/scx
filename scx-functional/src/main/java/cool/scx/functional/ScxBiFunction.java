package cool.scx.functional;

/// 支持显式抛出异常的 BiConsumer.
///
/// @param <T> 参数1类型
/// @param <U> 参数2类型
/// @param <R> 返回值类型
/// @param <E> 异常类型
/// @author scx567888
/// @version 0.0.1
/// @see java.util.function.BiFunction
@FunctionalInterface
public interface ScxBiFunction<T, U, R, E extends Throwable> {

    /// apply
    ///
    /// @param t the first function argument
    /// @param u the second function argument
    /// @return the function result
    R apply(T t, U u) throws E;

}
