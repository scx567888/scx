package cool.scx.functional;

/// 支持显式抛出异常的 Callable.
///
/// @param <R> 返回值类型
/// @param <E> 异常类型
/// @author scx567888
/// @version 0.0.1
/// @see java.util.concurrent.Callable
@FunctionalInterface
public interface ScxCallable<R, E extends Throwable> {

    R call() throws E;

}
