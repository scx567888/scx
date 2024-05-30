package cool.scx.common.util;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * 使用 threadLocal 模拟的 ScopedValue
 * todo ScopedValue 正式版本发布时 移除此类
 *
 * @param <T> a
 */
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

        public void run(Runnable op) {
            var exception = new AtomicReference<Exception>();
            var w = Thread.ofVirtual().name("scx-scoped-value-thread-", THREAD_NUMBER.getAndIncrement()).start(() -> {
                key.bind(value);
                try {
                    op.run();
                } catch (Exception e) {
                    exception.set(e);
                }
            });
            try {
                w.join();
            } catch (InterruptedException e) {
                throw new ScxExceptionHelper.ScxWrappedRuntimeException(e);
            }
            if (exception.get() != null) {
                throw new ScxExceptionHelper.ScxWrappedRuntimeException(exception.get());
            }
        }

        public <R> R get(Supplier<? extends R> op) {
            var result = new AtomicReference<R>();
            var exception = new AtomicReference<Exception>();
            var w = Thread.ofVirtual().name("scx-scoped-value-thread-", THREAD_NUMBER.getAndIncrement()).start(() -> {
                key.bind(value);
                try {
                    var r = op.get();
                    result.set(r);
                } catch (Exception e) {
                    exception.set(e);
                }
            });
            try {
                w.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (exception.get() != null) {
                throw new ScxExceptionHelper.ScxWrappedRuntimeException(exception.get());
            }
            return result.get();
        }

        public <R> R call(Callable<? extends R> op) throws Exception {
            var result = new AtomicReference<R>();
            var exception = new AtomicReference<Exception>();
            var w = Thread.ofVirtual().name("scx-scoped-value-thread-", THREAD_NUMBER.getAndIncrement()).start(() -> {
                key.bind(value);
                try {
                    var r = op.call();
                    result.set(r);
                } catch (Exception e) {
                    exception.set(e);
                }
            });
            try {
                w.join();
            } catch (InterruptedException e) {
                throw new ScxExceptionHelper.ScxWrappedRuntimeException(e);
            }
            if (exception.get() != null) {
                throw exception.get();
            }
            return result.get();
        }

    }

}
