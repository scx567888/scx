package cool.scx.util;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * MultiMap
 *
 * @param <K> Key
 * @param <V> Value
 * @author scx567888
 * @version 0.0.1
 */
public final class MultiMap<K, V> {

    private final Map<K, List<V>> map = new LinkedHashMap<>();

    private int size = 0;

    public Map<K, List<V>> toMultiValueMap() {
        return map;
    }

    public HashMap<K, V> toSingleValueMap() {
        var tempMap = new HashMap<K, V>();
        for (var e : map.entrySet()) {
            tempMap.put(e.getKey(), e.getValue().get(0));
        }
        return tempMap;
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public List<V> values() {
        return map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public List<V> get(K key) {
        return map.computeIfAbsent(key, k -> new ArrayList<>());
    }

    public boolean remove(K key, V value) {
        var collection = map.get(key);
        if (collection != null && collection.remove(value)) {
            size = size - 1;
            return true;
        }
        return false;
    }

    public List<V> removeAll(K key) {
        var collection = map.remove(key);
        if (collection == null) {
            return Collections.emptyList();
        }
        var output = new ArrayList<>(collection);
        size = size - collection.size();
        collection.clear();
        return Collections.unmodifiableList(output);
    }

    public boolean put(K key, V value) {
        var collection = get(key);
        collection.add(value);
        size = size + 1;
        return true;
    }

    public boolean putAll(K key, Collection<? extends V> values) {
        var collection = get(key);
        var add = collection.addAll(values);
        if (add) {
            size = size + values.size();
        }
        return add;
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public boolean containsValue(V value) {
        return map.values().stream().anyMatch(collection -> collection.contains(value));
    }

    public void clear() {
        for (var collection : map.values()) {
            collection.clear();
        }
        map.clear();
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach((key, valueCollection) -> valueCollection.forEach(value -> action.accept(key, value)));
    }

}
