package cool.scx.http;

import cool.scx.common.util.MultiMap;

import java.util.*;

/**
 * ParametersImpl
 */
public class ParametersImpl<K, V> implements ParametersWritable<K, V> {

    private final MultiMap<K, V> map = new MultiMap<>(LinkedHashMap::new, () -> new ArrayList<>(1));

    @SuppressWarnings("unchecked")
    @Override
    public ParametersImpl<K, V> set(K name, V... value) {
        map.setAll(name, List.of(value));
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ParametersImpl<K, V> add(K name, V... value) {
        map.putAll(name, List.of(value));
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
        return map.keySet();
    }

    @Override
    public V get(K name) {
        return map.getFirst(name);
    }

    @Override
    public List<V> getAll(K name) {
        return map.get(name);
    }

    @Override
    public Iterator<Map.Entry<K, List<V>>> iterator() {
        return map.toMultiValueMap().entrySet().iterator();
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
