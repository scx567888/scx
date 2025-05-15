package cool.scx.data.query;

/// WhereType
///
/// @author scx567888
/// @version 0.0.1
public enum WhereType {

    /// 等于 eq
    EQUAL,
    /// 不等于 ne
    NOT_EQUAL,
    /// 小于 lt
    LESS_THAN,
    /// 小于等于 lte
    LESS_THAN_OR_EQUAL,
    /// 大于 gt
    GREATER_THAN,
    /// 大于等于 gte
    GREATER_THAN_OR_EQUAL,
    /// 双端模糊匹配
    LIKE,
    /// 非双端模糊匹配
    NOT_LIKE,
    /// 正则表达式匹配
    LIKE_REGEX,
    /// 非正则表达式匹配
    NOT_LIKE_REGEX,
    /// 在集合内 (集合元素中 null 同样是合法匹配项,空集合表示不匹配任何 )
    IN,
    /// 不在集合内 (集合元素中 null 同样是合法匹配项,空集合表示不匹配任何 )
    NOT_IN,
    /// 范围包含 (包含上下界, 即 low <= field <= high)
    BETWEEN,
    /// 非范围包含 (包含上下界, 即 low <= field <= high)
    NOT_BETWEEN,
    /// JSON 包含某子结构
    JSON_CONTAINS,
    /// JSON 数组之间有交集
    JSON_OVERLAPS

}
