package cool.scx.collections.count_map;

import cool.scx.functional.ScxBiConsumer;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/// CountMap 接口 用来约束
///
/// @param <K> Key
/// @author scx567888
/// @version 0.0.1
public interface ICountMap<K> extends Iterable<Map.Entry<K, Long>> {

    /// 添加
    ///
    /// @param key   key
    /// @param count count
    /// @return 添加后的数量
    long add(K key, long count);

    /// 覆盖
    ///
    /// @param key   key
    /// @param count count
    /// @return 之前的数量 (可为空)
    Long set(K key, long count);

    /// 获取
    ///
    /// @param key key
    /// @return 数量 (可为空)
    Long get(K key);

    /// 包含
    boolean containsKey(K key);

    /// set
    ///
    /// @param key key
    /// @return 之前的数量 (可为空)
    Long remove(K key);

    Set<K> keys();

    long size();

    boolean isEmpty();

    void clear();

    Map<K, Long> toMap();

    Map<K, Long> toMap(Supplier<Map<K, Long>> mapSupplier);

    <E extends Throwable> void forEach(ScxBiConsumer<? super K, Long, E> action) throws E;

}
