package cool.scx.common.util;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * MultiMap
 *
 * @param <K> Key
 * @param <V> Value
 * @author scx567888
 * @version 0.0.1
 */
public final class MultiMap<K, V> {

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

    public Map<K, List<V>> toMultiValueMap() {
        return map;
    }

    public Map<K, V> toSingleValueMap() {
        return toSingleValueMap(HashMap::new);
    }

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

    public Set<K> keySet() {
        return map.keySet();
    }

    public List<V> values() {
        var list = listSupplier.get();
        for (var vs : map.values()) {
            list.addAll(vs);
        }
        return list;
    }

    /**
     * 永不返回 null
     *
     * @param key a
     * @return a
     */
    public List<V> get(K key) {
        return map.getOrDefault(key, listSupplier.get());
    }

    public V getFirst(K key) {
        var values = map.get(key);
        return values == null || values.isEmpty() ? null : values.get(0);
    }

    public boolean remove(K key, V value) {
        var v = map.get(key);
        if (v != null && v.remove(value)) {
            if (v.isEmpty()) {
                map.remove(key);
            }
            return true;
        }
        return false;
    }

    public List<V> removeAll(K key) {
        return map.remove(key);
    }

    public boolean put(K key, V value) {
        var v = map.computeIfAbsent(key, k -> listSupplier.get());
        return v.add(value);
    }

    public boolean putAll(K key, Collection<? extends V> values) {
        var v = map.computeIfAbsent(key, k -> listSupplier.get());
        return v.addAll(values);
    }

    public void putAll(Map<? extends K, ? extends V> v) {
        v.forEach(this::put);
    }

    public void putAll(MultiMap<? extends K, ? extends V> v) {
        for (var entry : v.map.entrySet()) {
            var key = entry.getKey();
            var values = entry.getValue();
            putAll(key, values);
        }
    }

    public void set(K key, V value) {
        var values = listSupplier.get();
        values.add(value);
        this.map.put(key, values);
    }

    public void setAll(Map<? extends K, ? extends V> values) {
        values.forEach(this::set);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public boolean containsValue(V value) {
        for (var values : map.values()) {
            if (values.contains(value)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        for (var value : map.values()) {
            value.clear();
        }
        map.clear();
    }

    public boolean isEmpty() {
        return size() == 0L;
    }

    public long size() {
        var size = 0L;
        for (var value : map.values()) {
            size = size + value.size();
        }
        return size;
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        for (var entry : map.entrySet()) {
            var key = entry.getKey();
            var values = entry.getValue();
            for (var value : values) {
                action.accept(key, value);
            }
        }
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

}
