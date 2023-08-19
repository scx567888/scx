package cool.scx.util;

import java.util.function.Function;

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

}
