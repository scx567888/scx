package cool.scx.common.util;

import cool.scx.common.count_map.CountMap;
import cool.scx.common.multi_map.MultiMap;

import java.util.function.Function;

/**
 * 未分类方法
 *
 * @author scx567888
 * @version 0.0.1
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

    public static <T> CountMap<T> countingBy(Iterable<T> list) {
        return countingBy(list, k -> k, t -> 1L);
    }

    public static <K, T> CountMap<K> countingBy(Iterable<T> list, Function<? super T, ? extends K> keyFn) {
        return countingBy(list, keyFn, t -> 1L);
    }

    public static <K, T> CountMap<K> countingBy(Iterable<T> list, Function<? super T, ? extends K> keyFn, Function<? super T, Long> countFn) {
        var countMap = new CountMap<K>();
        for (var t : list) {
            var key = keyFn.apply(t);
            var count = countFn.apply(t);
            if (count != null) {
                countMap.add(key, count);
            }
        }
        return countMap;
    }

    public static <T> CountMap<T> countingBy(T[] list) {
        return countingBy(list, k -> k, t -> 1L);
    }

    public static <K, T> CountMap<K> countingBy(T[] list, Function<? super T, ? extends K> keyFn) {
        return countingBy(list, keyFn, t -> 1L);
    }

    public static <K, T> CountMap<K> countingBy(T[] list, Function<? super T, ? extends K> keyFn, Function<? super T, Long> countFn) {
        var countMap = new CountMap<K>();
        for (var t : list) {
            var key = keyFn.apply(t);
            var count = countFn.apply(t);
            if (count != null) {
                countMap.add(key, count);
            }
        }
        return countMap;
    }

}
