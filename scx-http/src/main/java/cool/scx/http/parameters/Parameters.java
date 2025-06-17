package cool.scx.http.parameters;

import java.util.List;
import java.util.Map;
import java.util.Set;

/// Parameters 类似 MultiMap 但是分为 只读 和 可读可写 两种类型 , 以便实现更细粒度的控制 ( 默认实现 基于 MultiMap)
///
/// @author scx567888
/// @version 0.0.1
public interface Parameters<K, V> extends Iterable<ParameterEntry<K, V>> {

    static <K, V> ParametersWritable<K, V> of() {
        return new ParametersImpl<>();
    }

    static <K, V> ParametersWritable<K, V> of(Parameters<K, V> p) {
        return new ParametersImpl<>(p);
    }

    long size();

    Set<K> names();

    V get(K name);

    List<V> getAll(K name);

    boolean contains(K name);

    boolean isEmpty();

    Map<K, List<V>> toMap();

}
