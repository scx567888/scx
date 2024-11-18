package cool.scx.common.util;

import java.util.*;
import java.util.function.Supplier;

/**
 * MultiMap
 *
 * @param <K> Key
 * @param <V> Value
 * @author scx567888
 * @version 0.0.1
 */
public class MultiMap<K, V> implements MultiMapInterface<K, V> {

    private final Map<K, List<V>> map;
    private final Supplier<List<V>> listSupplier;

    /**
     * 指定内部的 map 实现和内部的 key 实现
     *
     * @param mapSupplier  mapSupplier
     * @param listSupplier listSupplier
     */
    public MultiMap(Supplier<Map<K, List<V>>> mapSupplier, Supplier<List<V>> listSupplier) {
        this.map = mapSupplier.get();
        this.listSupplier = listSupplier;
    }

    /**
     * 默认内部 map 使用 HashMap, key 使用 ArrayList
     */
    public MultiMap() {
        this(HashMap::new, ArrayList::new);
    }

    @Override
    public boolean add(K key, V value) {
        var v = map.computeIfAbsent(key, k -> listSupplier.get());
        return v.add(value);
    }

    @Override
    public boolean add(K key, V... values) {
        var v = map.computeIfAbsent(key, k -> listSupplier.get());
        return Collections.addAll(v, values);
    }

    @Override
    public boolean add(K key, Collection<V> values) {
        var v = map.computeIfAbsent(key, k -> listSupplier.get());
        return v.addAll(values);
    }

    @Override
    public void add(Map<? extends K, ? extends V> map) {
        map.forEach(this::add);
    }

    @Override
    public void add(MultiMapInterface<? extends K, ? extends V> map) {
        for (var entry : map) {
            add(entry.getKey(), entry.getValue());
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
        Collections.addAll(v, values);
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
    public void set(MultiMapInterface<? extends K, ? extends V> map) {
        for (var entry : map) {
            set(entry.getKey(), entry.getValue());
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
        var remove = v.remove(value);
        if (v.isEmpty()) {
            map.remove(key);
        }
        return remove;
    }

    @Override
    public boolean remove(K key, V... values) {
        var v = map.get(key);
        if (v == null) {
            return false;
        }
        var anyRemoved = false;
        for (V value : values) {
            anyRemoved = anyRemoved | v.remove(value);
        }
        if (v.isEmpty()) {
            map.remove(key);
        }
        return anyRemoved;
    }

    @Override
    public boolean remove(K key, Collection<? extends V> values) {
        var v = map.get(key);
        if (v == null) {
            return false;
        }
        var remove = v.removeAll(values);
        if (v.isEmpty()) {
            map.remove(key);
        }
        return remove;
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
    public Iterator<Map.Entry<K, List<V>>> iterator() {
        return map.entrySet().iterator();
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

}
