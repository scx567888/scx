package cool.scx.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parameters
 */
public interface Parameters<K, V> extends Iterable<Map.Entry<K, List<V>>> {

    static <K, V> ParametersWritable<K, V> of() {
        return new ParametersImpl<>();
    }

    long size();

    Set<K> names();

    V get(K name);

    List<V> getAll(K name);

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
