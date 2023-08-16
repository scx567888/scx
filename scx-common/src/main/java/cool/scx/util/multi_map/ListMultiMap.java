package cool.scx.util.multi_map;

import java.util.ArrayList;
import java.util.List;

public final class ListMultiMap<K, V> extends AbstractMultiMap<K, V, List<V>> {

    @Override
    List<V> createCollection() {
        return new ArrayList<>();
    }

}
