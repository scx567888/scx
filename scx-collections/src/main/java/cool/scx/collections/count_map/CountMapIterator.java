package cool.scx.collections.count_map;

import java.util.Iterator;
import java.util.Map;

/// CountMapIterator
///
/// @author scx567888
/// @version 0.0.1
class CountMapIterator<K> implements Iterator<ICountMapEntry<K>> {

    private final Iterator<Map.Entry<K, Long>> iterator;

    public CountMapIterator(Iterator<Map.Entry<K, Long>> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public ICountMapEntry<K> next() {
        var next = iterator.next();
        return new CountMapEntry<>(next.getKey(), next.getValue());
    }

}
