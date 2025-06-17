package cool.scx.collections.multi_map;

import java.util.List;

public record MultiMapEntry<K, V>(K key, List<V> all) implements IMultiMapEntry<K, V> {

    @Override
    public V first() {
        return all.isEmpty() ? null : all.get(0);
    }

}
