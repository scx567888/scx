package cool.scx.collections.multi_map;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/// MultiMapIterator
///
/// @author scx567888
/// @version 0.0.1
class MultiMapIterator<K, V> implements Iterator<IMultiMapEntry<K, V>> {

    private final Iterator<Map.Entry<K, List<V>>> iterator;

    public MultiMapIterator(Iterator<Map.Entry<K, List<V>>> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public MultiMapEntry<K, V> next() {
        var next = iterator.next();
        return new MultiMapEntry<>(next.getKey(), next.getValue());
    }

}
