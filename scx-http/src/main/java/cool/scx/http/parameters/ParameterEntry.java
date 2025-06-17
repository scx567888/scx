package cool.scx.http.parameters;

import cool.scx.collections.multi_map.IMultiMapEntry;

import java.util.List;

/// ParameterEntry 表示 HTTP 请求中的参数项，包含参数名与多个值.
/// 注意：此接口虽然在结构上与 {@link IMultiMapEntry} 完全一致 (即一个 key 对应多个值),
/// 但本质上是一个独立的概念，语义层面完全不同，因此未直接复用.
/// 主要区别在于:
/// 1. 命名不同: 使用 name(), value(), values() 而非 key(), first(), all()，贴合 HTTP 参数领域的术语.
/// 2. 语义不同: {@link IMultiMapEntry} 是通用数据结构条目; ParameterEntry 是 HTTP 参数视图，强调请求上下文.
/// 3. 类型抽象目的不同: 本接口更关注 "参数" 的角色语义, 而不是映射关系本身.
/// 设计上遵循 "语义清晰优先" 与 "最小惊讶原则", 避免语义漂移, 即使在功能上可复用.
public interface ParameterEntry<K, V> {

    K name();

    V value();

    List<V> values();

}
