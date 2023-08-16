package cool.scx.util.multi_map;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * MultiMap
 *
 * @param <K> Key
 * @param <V> Value
 * @author scx567888
 * @version 0.0.1
 */
public interface MultiMap<K, V> {

    Map<K, ? extends Collection<V>> toMultiValueMap();

    HashMap<K, V> toSingleValueMap();

    /**
     * 所有 key
     *
     * @return key
     */
    Set<K> keySet();

    /**
     * 所有值
     *
     * @return a
     */
    List<V> values();

    /**
     * 获取
     *
     * @param key a
     * @return a
     */
    Collection<V> get(K key);

    /**
     * 移除
     *
     * @param key   a
     * @param value a
     * @return a
     */
    boolean remove(K key, V value);

    /**
     * 移除全部
     *
     * @param key a
     * @return a
     */
    Collection<V> removeAll(K key);

    /**
     * 添加
     *
     * @param key   Key
     * @param value value
     * @return a
     */
    boolean put(K key, V value);

    /**
     * 添加多个
     *
     * @param key    Key
     * @param values values
     * @return a
     */
    boolean putAll(K key, Collection<? extends V> values);

    boolean containsKey(K key);

    boolean containsValue(V value);

    /**
     * 是否为空
     *
     * @return a
     */
    boolean isEmpty();

    /**
     * Clear
     */
    void clear();

    /**
     * Size
     *
     * @return a
     */
    int size();

    /**
     * for 循环
     *
     * @param action a
     */
    void forEach(BiConsumer<? super K, ? super V> action);

}
