package cool.scx.collections.count_map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/// CountMap
///
/// @param <K> Key
/// @author scx567888
/// @version 0.0.1
public class CountMap<K> implements ICountMap<K> {

    private final Map<K, AtomicLong> map;

    public CountMap(Supplier<Map<K, AtomicLong>> mapSupplier) {
        this.map = mapSupplier.get();
    }

    public CountMap() {
        this(HashMap::new);
    }

    @Override
    public long add(K key, long count) {
        return map.computeIfAbsent(key, k -> new AtomicLong()).addAndGet(count);
    }

    @Override
    public Long set(K key, long count) {
        var v = map.put(key, new AtomicLong(count));
        return v == null ? null : v.get();
    }

    @Override
    public Long get(K key) {
        var v = map.get(key);
        return v == null ? null : v.get();
    }

    @Override
    public Long remove(K key) {
        var v = map.remove(key);
        return v == null ? null : v.get();
    }

    @Override
    public long size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keys() {
        return map.keySet();
    }

    @Override
    public Map<K, Long> toMap(Supplier<Map<K, Long>> mapSupplier) {
        var result = mapSupplier.get();
        for (var entry : map.entrySet()) {
            result.put(entry.getKey(), entry.getValue().get());
        }
        return result;
    }

    @Override
    public Map<K, Long> toMap() {
        return toMap(HashMap::new);
    }

    @Override
    public Iterator<Map.Entry<K, Long>> iterator() {
        return new CountMapIterator<>(map.entrySet().iterator());
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

}
