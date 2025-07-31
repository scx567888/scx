package cool.scx.http.parameters;

import cool.scx.function.BiConsumerX;

import java.util.List;
import java.util.Map;
import java.util.Set;

/// Parameters 表示一组 HTTP 参数项, 类似 MultiMap (实际上默认实现 就是基于 MultiMap 的), 一个 name 可以对应多个值.
///
/// 此接口专为 HTTP 场景设计, 而非复用通用 MultiMap, 原因包括:
/// 1. 权限分离: 需要区分为 只读 和 可读可写 两种类型, 以便实现更细粒度的控制.
/// 2. 语义聚焦: 命名更贴合 HTTP 参数语义, 如 names() 而非 keys().
/// 3. 行为收敛: 只暴露参数语义所需的最小方法集, 避免误用通用集合操作, 强调其领域语义的独立性.
///
/// 这让其成为语义更清晰, 行为更受控的 HTTP 参数视图, 而非通用数据结构的一种变体.
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

    Map<K, List<V>> toMultiValueMap();

    Map<K, V> toMap();

    <X extends Throwable> void forEach(BiConsumerX<? super K, V, X> action) throws X;

    <X extends Throwable> void forEachParameter(BiConsumerX<? super K, List<V>, X> action) throws X;

}
