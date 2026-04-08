package cool.scx.collections.count_map;

import cool.scx.function.Function2Void;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/// ICountMap
///
/// @author scx567888
/// @version 0.0.1
public interface ICountMap<K> extends Iterable<ICountMapEntry<K>> {

    /// 返回 添加后的数量
    long add(K key, long count);

    /// 返回 之前的数量 (可为空)
    Long set(K key, long count);

    /// 返回 数量 (可为空)
    Long get(K key);

    //********** 包含 ****************
    boolean containsKey(K key);

    /// 返回 之前的数量 (可为空)
    Long remove(K key);

    //********** 基本值 **********
    Set<K> keys();

    //*********** 基本功能 ************
    long size();

    boolean isEmpty();

    void clear();

    //*********** 转换方法 ************
    Map<K, Long> toMap();

    Map<K, Long> toMap(Supplier<Map<K, Long>> mapSupplier);

    <X extends Throwable> void forEach(Function2Void<? super K, Long, X> action) throws X;

}
