package cool.scx.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parameters 类似 MultiMap 但是分为 只读 和 可读可写 两种类型 , 以便实现更细粒度的控制 ( 默认实现 基于 MultiMap)
 */
public interface Parameters<K, V> extends Iterable<Map.Entry<K, List<V>>> {

    static <K, V> ParametersWritable<K, V> of() {
        return new ParametersImpl<>();
    }

    static <K, V> ParametersWritable<K, V> of(Parameters<K,V> p) {
        return new ParametersImpl<>(p);
    }

    long size();

    Set<K> names();

    V get(K name);

    List<V> getAll(K name);

    default boolean contains(K name) {
        return get(name) != null;
    }

    default boolean isEmpty() {
        return this.size() == 0;
    }

    default Map<K, List<V>> toMap() {
        var map = new HashMap<K, List<V>>();
        for (var stringListEntry : this) {
            map.put(stringListEntry.getKey(), stringListEntry.getValue());
        }
        return map;
    }

}
