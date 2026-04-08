package cool.scx.collections.multi_map;

import cool.scx.function.Function2Void;

import java.util.*;
import java.util.function.Supplier;

/// MultiMap
///
/// @author scx567888
/// @version 0.0.1
@SuppressWarnings("unchecked")
public class MultiMap<K, V> implements IMultiMap<K, V> {

    private final Map<K, List<V>> map;
    private final Supplier<List<V>> listSupplier;

    /// 默认内部 map 使用 HashMap, list 使用 ArrayList
    public MultiMap() {
        this(HashMap::new, ArrayList::new);
    }

    /// 指定内部的 map 实现和内部的 list 实现
    public MultiMap(Supplier<Map<K, List<V>>> mapSupplier, Supplier<List<V>> listSupplier) {
        this.map = mapSupplier.get();
        this.listSupplier = listSupplier;
    }

    @Override
    public boolean add(K key, V value) {
        var v = map.computeIfAbsent(key, k -> listSupplier.get());
        return v.add(value);
    }

    @Override
    public boolean add(K key, V... values) {
        var v = map.computeIfAbsent(key, k -> listSupplier.get());
        return v.addAll(List.of(values));
    }

    @Override
    public boolean add(K key, Collection<? extends V> values) {
        var v = map.computeIfAbsent(key, k -> listSupplier.get());
        return v.addAll(values);
    }

    @Override
    public void add(Map<? extends K, ? extends V> map) {
        map.forEach(this::add);
    }

    @Override
    public void add(IMultiMap<? extends K, ? extends V> map) {
        for (var entry : map) {
            add(entry.key(), entry.values());
        }
    }

    @Override
    public List<V> set(K key, V value) {
        var v = listSupplier.get();
        v.add(value);
        return this.map.put(key, v);
    }

    @Override
    public List<V> set(K key, V... values) {
        var v = listSupplier.get();
        v.addAll(List.of(values));
        return this.map.put(key, v);
    }

    @Override
    public List<V> set(K key, Collection<? extends V> values) {
        var v = listSupplier.get();
        v.addAll(values);
        return this.map.put(key, v);
    }

    @Override
    public void set(Map<? extends K, ? extends V> map) {
        map.forEach(this::set);
    }

    @Override
    public void set(IMultiMap<? extends K, ? extends V> map) {
        for (var entry : map) {
            set(entry.key(), entry.values());
        }
    }

    @Override
    public V get(K key) {
        var values = map.get(key);
        return values == null || values.size() == 0 ? null : values.get(0);
    }

    @Override
    public List<V> getAll(K key) {
        return map.getOrDefault(key, listSupplier.get());
    }

    @Override
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        for (var values : map.values()) {
            if (values.contains(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(K key, V value) {
        var v = map.get(key);
        if (v == null) {
            return false;
        }
        // 并发操作时可能存在空列表残留问题, 但并不影响后续逻辑
        var removed = v.remove(value);
        if (v.isEmpty()) {
            map.remove(key);
        }
        return removed;
    }

    @Override
    public boolean remove(K key, V... values) {
        var v = map.get(key);
        if (v == null) {
            return false;
        }
        // 并发操作时可能存在空列表残留问题, 但并不影响后续逻辑
        var removed = v.removeAll(List.of(values));
        if (v.isEmpty()) {
            map.remove(key);
        }
        return removed;
    }

    @Override
    public boolean remove(K key, Collection<? extends V> values) {
        var v = map.get(key);
        if (v == null) {
            return false;
        }
        // 并发操作时可能存在空列表残留问题, 但并不影响后续逻辑
        var removed = v.removeAll(values);
        if (v.isEmpty()) {
            map.remove(key);
        }
        return removed;
    }

    @Override
    public List<V> removeAll(K key) {
        return map.remove(key);
    }

    @Override
    public Set<K> keys() {
        return map.keySet();
    }

    @Override
    public List<V> values() {
        var list = listSupplier.get();
        for (var v : map.values()) {
            list.addAll(v);
        }
        return list;
    }

    @Override
    public long size() {
        var size = 0L;
        for (var value : map.values()) {
            size = size + value.size();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0L;
    }

    @Override
    public void clear() {
        for (var value : map.values()) {
            value.clear();
        }
        map.clear();
    }

    @Override
    public Map<K, List<V>> toMultiValueMap() {
        return map;
    }

    public Map<K, V> toSingleValueMap() {
        return toSingleValueMap(HashMap::new);
    }

    @Override
    public Map<K, V> toSingleValueMap(Supplier<Map<K, V>> mapSupplier) {
        var tempMap = mapSupplier.get();
        for (var e : map.entrySet()) {
            var key = e.getKey();
            var value = e.getValue();
            if (value != null && !value.isEmpty()) {
                tempMap.put(key, value.get(0));
            }
        }
        return tempMap;
    }

    @Override
    public <X extends Throwable> void forEach(Function2Void<? super K, V, X> action) throws X {
        for (var entry : map.entrySet()) {
            var key = entry.getKey();
            var values = entry.getValue();
            for (var value : values) {
                action.apply(key, value);
            }
        }
    }

    @Override
    public <X extends Throwable> void forEachEntry(Function2Void<? super K, List<V>, X> action) throws X {
        for (var entry : map.entrySet()) {
            var key = entry.getKey();
            var values = entry.getValue();
            action.apply(key, values);
        }
    }

    @Override
    public Iterator<IMultiMapEntry<K, V>> iterator() {
        return new MultiMapIterator<>(map.entrySet().iterator());
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof MultiMap<?, ?> o) {
            return map.equals(o.map);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

}
