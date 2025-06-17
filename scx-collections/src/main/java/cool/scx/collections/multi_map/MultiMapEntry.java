package cool.scx.collections.multi_map;

import java.util.List;

/// MultiMapEntry
///
/// @param key key
/// @param all all
/// @param <K> K
/// @param <V> V
/// @author scx567888
/// @version 0.0.1
record MultiMapEntry<K, V>(K key, List<V> all) implements IMultiMapEntry<K, V> {

    @Override
    public V first() {
        return all.isEmpty() ? null : all.get(0);
    }

}
