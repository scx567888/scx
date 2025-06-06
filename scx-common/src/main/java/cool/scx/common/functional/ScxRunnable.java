package cool.scx.common.functional;

/// 支持显式抛出异常的 Runnable.
///
/// @param <E> 异常类型
/// @author scx567888
/// @version 0.0.1
/// @see java.lang.Runnable
@FunctionalInterface
public interface ScxRunnable<E extends Throwable> {

    /// run
    ///
    /// @throws E 异常
    void run() throws E;

}
