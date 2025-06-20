package cool.scx.common.scope_value;

import cool.scx.functional.ScxCallable;
import cool.scx.functional.ScxRunnable;

import java.util.ArrayDeque;
import java.util.Deque;

/// 使用 threadLocal 模拟的 ScopedValue (不支持线程池线程复用和复杂异步线程切换)
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

        private void bindAll(Deque<Object> oldValues) {
            if (prev != null) {
                prev.bindAll(oldValues);
            }
            oldValues.addLast(key.get());
            key.bind(value);
        }

        private void restoreAll(Deque<Object> oldValues) {
            var old = oldValues.pollLast();
            key.bind(old);
            if (prev != null) {
                prev.restoreAll(oldValues);
            }
        }

        public <T> Carrier where(ScxScopedValue<T> key, T value) {
            return new Carrier(key, value, this);
        }

        public <E extends Throwable> void run(ScxRunnable<E> op) throws E {
            var oldValues = new ArrayDeque<>();
            bindAll(oldValues);
            try {
                op.run();
            } finally {
                restoreAll(oldValues);
            }
        }

        public <R, E extends Throwable> R call(ScxCallable<? extends R, E> op) throws E {
            var oldValues = new ArrayDeque<>();
            bindAll(oldValues);
            try {
                return op.call();
            } finally {
                restoreAll(oldValues);
            }
        }

    }

}
