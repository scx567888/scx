package cool.scx.common.util;

import cool.scx.functional.ScxCallable;
import cool.scx.functional.ScxRunnable;
import cool.scx.functional.ScxSupplier;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/// 使用 threadLocal 模拟的 ScopedValue
/// todo ScopedValue 正式版本发布时 移除此类
///
/// @param <T> a
/// @author scx567888
/// @version 0.0.1
public final class ScopedValue<T> {

    private static final AtomicLong THREAD_NUMBER = new AtomicLong(0);
    private final InheritableThreadLocal<T> threadLocal = new InheritableThreadLocal<>();

    public static <T> ScopedValue<T> newInstance() {
        return new ScopedValue<>();
    }

    public static <T> Carrier<T> where(ScopedValue<T> key, T value) {
        return new Carrier<>(key, value);
    }

    void bind(T value) {
        threadLocal.set(value);
    }

    public T get() {
        return threadLocal.get();
    }

    public static final class Carrier<T> {

        private final T value;
        private final ScopedValue<T> key;

        public Carrier(ScopedValue<T> key, T value) {
            this.key = key;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        public <E extends Throwable> void run(ScxRunnable<E> op) throws E {
            var exception = new AtomicReference<E>();
            var w = Thread.ofPlatform().name("scx-scoped-value-thread-", THREAD_NUMBER.getAndIncrement()).start(() -> {
                key.bind(value);
                try {
                    op.run();
                } catch (Throwable e) {
                    exception.set((E) e);
                }
            });
            try {
                w.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            if (exception.get() != null) {
                throw exception.get();
            }
        }

        @SuppressWarnings("unchecked")
        public <R, E extends Throwable> R get(ScxSupplier<? extends R, E> op) throws E {
            var result = new AtomicReference<R>();
            var exception = new AtomicReference<E>();
            var w = Thread.ofPlatform().name("scx-scoped-value-thread-", THREAD_NUMBER.getAndIncrement()).start(() -> {
                key.bind(value);
                try {
                    var r = op.get();
                    result.set(r);
                } catch (Throwable e) {
                    exception.set((E) e);
                }
            });
            try {
                w.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            if (exception.get() != null) {
                throw exception.get();
            }
            return result.get();
        }

        public <R, E extends Throwable> R call(ScxCallable<? extends R, E> op) throws E {
            var result = new AtomicReference<R>();
            var exception = new AtomicReference<E>();
            var w = Thread.ofPlatform().name("scx-scoped-value-thread-", THREAD_NUMBER.getAndIncrement()).start(() -> {
                key.bind(value);
                try {
                    var r = op.call();
                    result.set(r);
                } catch (Throwable e) {
                    exception.set((E) e);
                }
            });
            try {
                w.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            if (exception.get() != null) {
                throw exception.get();
            }
            return result.get();
        }

    }

}
