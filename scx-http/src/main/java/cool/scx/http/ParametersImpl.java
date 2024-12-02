package cool.scx.http;

import cool.scx.common.multi_map.MultiMap;

import java.util.*;

/**
 * ParametersImpl
 */
public class ParametersImpl<K, V> implements ParametersWritable<K, V> {

    private final MultiMap<K, V> map;

    public ParametersImpl(Parameters<K, V> p) {
        this();
        for (var e : p) {
            this.map.set(e.getKey(), e.getValue());
        }
    }

    public ParametersImpl() {
        this.map = new MultiMap<>(LinkedHashMap::new, ArrayList::new);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ParametersImpl<K, V> set(K name, V... value) {
        map.set(name, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ParametersImpl<K, V> add(K name, V... value) {
        map.add(name, value);
        return this;
    }

    @Override
    public ParametersImpl<K, V> remove(K name) {
        map.removeAll(name);
        return this;
    }

    @Override
    public ParametersWritable<K, V> clear() {
        map.clear();
        return this;
    }

    @Override
    public long size() {
        return map.size();
    }

    @Override
    public Set<K> names() {
        return map.keys();
    }

    @Override
    public V get(K name) {
        return map.get(name);
    }

    @Override
    public List<V> getAll(K name) {
        return map.getAll(name);
    }

    @Override
    public Iterator<Map.Entry<K, List<V>>> iterator() {
        return map.iterator();
    }

    @Override
    public Map<K, List<V>> toMap() {
        return map.toMultiValueMap();
    }

    @Override
    public String toString() {
        return map.toString();
    }

}
