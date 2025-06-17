package cool.scx.collections.multi_map;

import cool.scx.functional.ScxBiConsumer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/// MultiMap 接口 用来约束
///
/// @param <K> Key
/// @param <V> Value
/// @author scx567888
/// @version 0.0.1
@SuppressWarnings("unchecked")
public interface IMultiMap<K, V> extends Iterable<IMultiMapEntry<K, V>> {

    //*********** 添加 ************
    boolean add(K key, V value);

    boolean add(K key, V... values);

    boolean add(K key, Collection<? extends V> values);

    void add(Map<? extends K, ? extends V> map);

    void add(IMultiMap<? extends K, ? extends V> map);


    //********** 覆盖 **************
    List<V> set(K key, V value);

    List<V> set(K key, V... values);

    List<V> set(K key, Collection<? extends V> values);

    void set(Map<? extends K, ? extends V> map);

    void set(IMultiMap<? extends K, ? extends V> map);


    //********** 获取 **************
    V get(K key);

    List<V> getAll(K key);

    //********** 包含 ****************
    boolean containsKey(K key);

    boolean containsValue(V value);


    //*********** 移除 ****************
    boolean remove(K key, V value);

    boolean remove(K key, V... values);

    boolean remove(K key, Collection<? extends V> values);

    List<V> removeAll(K key);

    //********** 基本值 **********
    Set<K> keys();

    /// 返回 MultiMap 中所有值的扁平集合 (所有键对应的 values 合并成一个列表)
    List<V> values();

    //*********** 基本功能 ************

    /// 返回 MultiMap 中所有键对应的所有值的总数量 (即所有 values 的扁平总和)
    long size();

    boolean isEmpty();

    void clear();

    //*********** 转换方法 ************
    Map<K, List<V>> toMultiValueMap();

    Map<K, V> toSingleValueMap();

    Map<K, V> toSingleValueMap(Supplier<Map<K, V>> mapSupplier);

    <E extends Throwable> void forEach(ScxBiConsumer<? super K, V, E> action) throws E;

    <E extends Throwable> void forEachEntry(ScxBiConsumer<? super K, List<V>, E> action) throws E;

}
