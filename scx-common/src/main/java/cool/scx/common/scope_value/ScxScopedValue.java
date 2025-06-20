package cool.scx.common.scope_value;

import cool.scx.functional.ScxCallable;
import cool.scx.functional.ScxRunnable;

/// 使用 threadLocal 模拟的 ScopedValue
/// todo ScopedValue 正式版本发布时 移除此类
///
/// @param <T> a
/// @author scx567888
/// @version 0.0.1
public final class ScxScopedValue<T> {

    private final InheritableThreadLocal<T> threadLocal = new InheritableThreadLocal<>();

    public static <T> ScxScopedValue<T> newInstance() {
        return new ScxScopedValue<>();
    }

    public static <T> Carrier where(ScxScopedValue<T> key, T value) {
        return new Carrier(key, value, null);
    }

    @SuppressWarnings("unchecked")
    void bind(Object value) {
        threadLocal.set((T) value);
    }

    public T get() {
        return threadLocal.get();
    }

    public static final class Carrier {

        private final ScxScopedValue<?> key;
        private final Object value;
        private final Carrier prev;

        public Carrier(ScxScopedValue<?> key, Object value, Carrier prev) {
            this.key = key;
            this.value = value;
            this.prev = prev;
        }

        public <T> Carrier where(ScxScopedValue<T> key, T value) {
            return new Carrier(key, value, this);
        }

        public <E extends Throwable> void run(ScxRunnable<E> op) throws E {
            var old = key.get();
            key.bind(value);
            try {
                op.run();
            } finally {
                key.bind(old);
            }
        }

        public <R, E extends Throwable> R call(ScxCallable<? extends R, E> op) throws E {
            var old = key.get();
            key.bind(value);
            try {
                return op.call();
            } finally {
                key.bind(old);
            }
        }

    }

}
