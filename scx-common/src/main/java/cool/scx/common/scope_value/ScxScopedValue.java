package cool.scx.common.scope_value;

import cool.scx.functional.ScxCallable;
import cool.scx.functional.ScxRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/// 使用 threadLocal 模拟的 ScopedValue (不支持线程池线程复用和复杂异步线程切换)
/// todo ScopedValue 正式版本发布时 移除此类
///
/// @param <T> a
/// @author scx567888
/// @version 0.0.1
public final class ScxScopedValue<T> {

    private final static Object UNBOUND = new Object();

    private final InheritableThreadLocal<Object> threadLocal = new InheritableThreadLocal<>() {
        @Override
        protected Object initialValue() {
            return UNBOUND;
        }
    };

    public static <T> ScxScopedValue<T> newInstance() {
        return new ScxScopedValue<>();
    }

    public static <T> Carrier where(ScxScopedValue<T> key, T value) {
        return new Carrier(key, value, null);
    }

    @SuppressWarnings("unchecked")
    public T get() {
        var value = threadLocal.get();
        if (value == UNBOUND) {
            throw new NoSuchElementException("ScopedValue not bound");
        }
        return (T) value;
    }

    public boolean isBound() {
        var value = threadLocal.get();
        return value != UNBOUND;
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

        private void bindAll(List<Object> oldValues) {
            if (prev != null) {
                prev.bindAll(oldValues);
            }
            oldValues.addLast(key.threadLocal.get());
            key.threadLocal.set(value);
        }

        private void restoreAll(List<Object> oldValues) {
            var old = oldValues.removeLast();
            key.threadLocal.set(old);
            if (prev != null) {
                prev.restoreAll(oldValues);
            }
        }

        public <T> Carrier where(ScxScopedValue<T> key, T value) {
            return new Carrier(key, value, this);
        }

        public <E extends Throwable> void run(ScxRunnable<E> op) throws E {
            var oldValues = new ArrayList<>();
            bindAll(oldValues);
            try {
                op.run();
            } finally {
                restoreAll(oldValues);
            }
        }

        public <R, E extends Throwable> R call(ScxCallable<? extends R, E> op) throws E {
            var oldValues = new ArrayList<>();
            bindAll(oldValues);
            try {
                return op.call();
            } finally {
                restoreAll(oldValues);
            }
        }

    }

}
