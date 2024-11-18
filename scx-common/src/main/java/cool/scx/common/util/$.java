package cool.scx.common.util;

import cool.scx.common.functional.ScxRunnable;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static cool.scx.common.util.ScxExceptionHelper.getRootCause;

/**
 * 未分类方法
 */
public final class $ {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {

        }
    }

    public static CompletableFuture<Void> async(ScxRunnable<?> runnable) {
        var promise = new CompletableFuture<Void>();
        Thread.ofVirtual().start(() -> {
            try {
                runnable.run();
                promise.complete(null);
            } catch (Throwable e) {
                promise.completeExceptionally(e);
            }
        });
        return promise;
    }

    public static <T> CompletableFuture<T> async(Callable<T> callable) {
        var promise = new CompletableFuture<T>();
        Thread.ofVirtual().start(() -> {
            try {
                var t = callable.call();
                promise.complete(t);
            } catch (Throwable e) {
                promise.completeExceptionally(e);
            }
        });
        return promise;
    }

    public static <T> T await(CompletableFuture<T> promise) throws Throwable {
        try {
            return promise.get();
        } catch (Exception e) {
            throw getRootCause(e);
        }
    }

    public static <K, T> MultiMap<K, T> groupingBy(Iterable<T> list, Function<? super T, ? extends K> keyFn) {
        return groupingBy(list, keyFn, t -> t);
    }

    public static <K, V, T> MultiMap<K, V> groupingBy(Iterable<T> list, Function<? super T, ? extends K> keyFn, Function<? super T, ? extends V> valueFn) {
        var multiMap = new MultiMap<K, V>();
        for (var t : list) {
            var key = keyFn.apply(t);
            var value = valueFn.apply(t);
            multiMap.put(key, value);
        }
        return multiMap;
    }

    public static <K, T> MultiMap<K, T> groupingBy(T[] list, Function<? super T, ? extends K> keyFn) {
        return groupingBy(list, keyFn, t -> t);
    }

    public static <K, V, T> MultiMap<K, V> groupingBy(T[] list, Function<? super T, ? extends K> keyFn, Function<? super T, ? extends V> valueFn) {
        var multiMap = new MultiMap<K, V>();
        for (var t : list) {
            var key = keyFn.apply(t);
            var value = valueFn.apply(t);
            multiMap.put(key, value);
        }
        return multiMap;
    }

}
