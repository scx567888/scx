package cool.scx.common.util;

import java.util.function.Function;

/**
 * 未分类方法
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class $ {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {

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
            multiMap.add(key, value);
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
            multiMap.add(key, value);
        }
        return multiMap;
    }

}
