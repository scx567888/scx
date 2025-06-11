package cool.scx.functional;


/// 支持显式抛出异常的 Supplier.
///
/// @param <R> 返回值类型
/// @param <E> 异常类型
/// @author scx567888
/// @version 0.0.1
/// @see java.util.function.Supplier
@FunctionalInterface
public interface ScxSupplier<R, E extends Throwable> {

    R get() throws E;

}
