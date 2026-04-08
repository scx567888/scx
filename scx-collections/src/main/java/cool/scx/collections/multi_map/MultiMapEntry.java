package cool.scx.collections.multi_map;

import java.util.List;

/// MultiMapEntry
///
/// @author scx567888
/// @version 0.0.1
record MultiMapEntry<K, V>(K key, List<V> values) implements IMultiMapEntry<K, V> {

    @Override
    public V value() {
        return values.isEmpty() ? null : values.get(0);
    }

}
