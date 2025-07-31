package cool.scx.http.parameters;

import cool.scx.collections.multi_map.MultiMap;
import cool.scx.function.BiConsumerX;

import java.util.*;

/// ParametersImpl
///
/// @author scx567888
/// @version 0.0.1
public class ParametersImpl<K, V> implements ParametersWritable<K, V> {

    private final MultiMap<K, V> map;

    public ParametersImpl(Parameters<K, V> p) {
        this();
        for (var e : p) {
            this.map.set(e.name(), e.values());
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
    public boolean contains(K name) {
        return map.containsKey(name);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public Map<K, List<V>> toMultiValueMap() {
        return map.toMultiValueMap();
    }

    @Override
    public Map<K, V> toMap() {
        return map.toSingleValueMap();
    }

    @Override
    public <E extends Throwable> void forEach(BiConsumerX<? super K, V, E> action) throws E {
        map.forEach(action);
    }

    @Override
    public <E extends Throwable> void forEachParameter(BiConsumerX<? super K, List<V>, E> action) throws E {
        map.forEachEntry(action);
    }

    @Override
    public Iterator<ParameterEntry<K, V>> iterator() {
        return new ParametersIterator<>(map.iterator());
    }

    @Override
    public String toString() {
        return map.toString();
    }

}
