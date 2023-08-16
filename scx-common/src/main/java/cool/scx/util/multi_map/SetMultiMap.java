package cool.scx.util.multi_map;

import java.util.HashSet;
import java.util.Set;

public final class SetMultiMap<K, V> extends AbstractMultiMap<K, V, Set<V>> {

    @Override
    Set<V> createCollection() {
        return new HashSet<>();
    }

}
