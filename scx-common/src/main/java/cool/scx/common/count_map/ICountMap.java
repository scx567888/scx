package cool.scx.common.count_map;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/// CountMap 接口 用来约束
///
/// @param <K> Key
/// @author scx567888
/// @version 0.0.1
public interface ICountMap<K> extends Iterable<Map.Entry<K, Long>> {

    /// add
    ///
    /// @param key   key
    /// @param count count
    /// @return 添加后的数量
    long add(K key, long count);

    /// set
    ///
    /// @param key   key
    /// @param count count
    /// @return 之前的数量 (可为空)
    Long set(K key, long count);

    /// get
    ///
    /// @param key key
    /// @return 数量 (可为空)
    Long get(K key);

    /// set
    ///
    /// @param key key
    /// @return 之前的数量 (可为空)
    Long remove(K key);

    long size();

    boolean isEmpty();

    void clear();

    Set<K> keys();

    Map<K, Long> toMap();

    Map<K, Long> toMap(Supplier<Map<K, Long>> mapSupplier);

}
