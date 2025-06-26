package cool.scx.collections.count_map;

import cool.scx.functional.ScxObjLongConsumer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/// CountMap
///
/// @author scx567888
/// @version 0.0.1
public class CountMap<K> implements ICountMap<K> {

    private final Map<K, AtomicLong> map;

    /// 默认内部 map 使用 HashMap
    public CountMap() {
        this(HashMap::new);
    }

    /// 指定内部的 map 实现
    public CountMap(Supplier<Map<K, AtomicLong>> mapSupplier) {
        this.map = mapSupplier.get();
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
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    @Override
    public Long remove(K key) {
        var v = map.remove(key);
        return v == null ? null : v.get();
    }

    @Override
    public Set<K> keys() {
        return map.keySet();
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
    public Map<K, Long> toMap() {
        return toMap(HashMap::new);
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
    public <E extends Throwable> void forEach(ScxObjLongConsumer<? super K, E> action) throws E {
        for (var entry : map.entrySet()) {
            var key = entry.getKey();
            var values = entry.getValue().get();
            action.accept(key, values);
        }
    }

    @Override
    public Iterator<ICountMapEntry<K>> iterator() {
        return new CountMapIterator<>(map.entrySet().iterator());
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

}
