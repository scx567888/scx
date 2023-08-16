package cool.scx.util.multi_map;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

abstract class AbstractMultiMap<K, V, C extends Collection<V>> implements MultiMap<K, V> {

    private final Map<K, C> map;
    private int size = 0;

    public AbstractMultiMap(Supplier<Map<K, C>> mapSupplier) {
        map = mapSupplier.get();
    }

    public AbstractMultiMap() {
        this(LinkedHashMap::new);
    }

    @Override
    public Map<K, C> toMultiValueMap() {
        return map;
    }

    @Override
    public HashMap<K, V> toSingleValueMap() {
        var tempMap = new HashMap<K, V>();
        for (var e : map.entrySet()) {
            tempMap.put(e.getKey(), e.getValue().iterator().next());
        }
        return tempMap;
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public C get(K key) {
        return map.computeIfAbsent(key, k -> createCollection());
    }

    @Override
    public boolean remove(K key, V value) {
        var collection = map.get(key);
        if (collection != null && collection.remove(value)) {
            size = size - 1;
            return true;
        }
        return false;
    }

    @Override
    public Collection<V> removeAll(K key) {
        var collection = map.remove(key);
        if (collection == null) {
            return Collections.emptyList();
        }
        var output = createCollection();
        output.addAll(collection);
        size = size - collection.size();
        collection.clear();
        return Collections.unmodifiableCollection(output);
    }

    @Override
    public boolean put(K key, V value) {
        var collection = get(key);
        var add = collection.add(value);
        if (add) {
            size = size + 1;
        }
        return add;
    }

    @Override
    public boolean putAll(K key, Collection<? extends V> values) {
        var collection = get(key);
        var add = collection.addAll(values);
        if (add) {
            size = size + values.size();
        }
        return add;
    }

    @Override
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        return map.values().stream().anyMatch(collection -> collection.contains(value));
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (var collection : map.values()) {
            collection.clear();
        }
        map.clear();
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach((key, valueCollection) -> valueCollection.forEach(value -> action.accept(key, value)));
    }

    abstract C createCollection();

}
