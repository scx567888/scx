package cool.scx.common.util;

import cool.scx.collections.ScxCollections;
import cool.scx.collections.count_map.CountMap;
import cool.scx.collections.multi_map.MultiMap;

import java.util.function.Function;

/// 未分类方法
///
/// @author scx567888
/// @version 0.0.1
public final class $ {

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {

        }
    }

    public static <K, T> MultiMap<K, T> groupingBy(Iterable<T> list, Function<? super T, ? extends K> keyFn) {
        return ScxCollections.groupingBy(list, keyFn);
    }

    public static <K, V, T> MultiMap<K, V> groupingBy(Iterable<T> list, Function<? super T, ? extends K> keyFn, Function<? super T, ? extends V> valueFn) {
        return ScxCollections.groupingBy(list, keyFn, valueFn);
    }

    public static <K, T> MultiMap<K, T> groupingBy(T[] list, Function<? super T, ? extends K> keyFn) {
        return ScxCollections.groupingBy(list, keyFn);
    }

    public static <K, V, T> MultiMap<K, V> groupingBy(T[] list, Function<? super T, ? extends K> keyFn, Function<? super T, ? extends V> valueFn) {
        return ScxCollections.groupingBy(list, keyFn, valueFn);
    }

    public static <T> CountMap<T> countingBy(Iterable<T> list) {
        return ScxCollections.countingBy(list);
    }

    public static <K, T> CountMap<K> countingBy(Iterable<T> list, Function<? super T, ? extends K> keyFn) {
        return ScxCollections.countingBy(list, keyFn);
    }

    public static <K, T> CountMap<K> countingBy(Iterable<T> list, Function<? super T, ? extends K> keyFn, Function<? super T, Long> countFn) {
        return ScxCollections.countingBy(list, keyFn, countFn);
    }

    public static <T> CountMap<T> countingBy(T[] list) {
        return ScxCollections.countingBy(list);
    }

    public static <K, T> CountMap<K> countingBy(T[] list, Function<? super T, ? extends K> keyFn) {
        return ScxCollections.countingBy(list, keyFn);
    }

    public static <K, T> CountMap<K> countingBy(T[] list, Function<? super T, ? extends K> keyFn, Function<? super T, Long> countFn) {
        return ScxCollections.countingBy(list, keyFn, countFn);
    }

}
