package cool.scx.common.functional;

/// 支持显式抛出异常的 BiConsumer.
///
/// @param <T> 参数1类型
/// @param <U> 参数2类型
/// @param <E> 异常类型
/// @author scx567888
/// @version 0.0.1
/// @see java.util.function.BiConsumer
@FunctionalInterface
public interface ScxBiConsumer<T, U, E extends Throwable> {

    /// accept
    ///
    /// @param t the first input argument
    /// @param u the second input argument
    void accept(T t, U u) throws E;

}
