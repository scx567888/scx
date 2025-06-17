package cool.scx.functional;

/// 支持显式抛出异常的 ObjLongConsumer.
///
/// @param <T> 参数1类型
/// @param <E> 异常类型
/// @author scx567888
/// @version 0.0.1
/// @see java.util.function.ObjLongConsumer
@FunctionalInterface
public interface ScxObjLongConsumer<T, E extends Throwable> {

    /// Performs this operation on the given arguments.
    ///
    /// @param t     the first input argument
    /// @param value the second input argument
    void accept(T t, long value) throws E;

}
