package cool.scx.data.build_control;

import cool.scx.data.query.WhereType;

public enum BuildControl {

    //**************** DSL 控制 *********************

    /// 替换现有 (只适用于 and 和 or)
    REPLACE,

    /// 如果查询的参数值为 null 则跳过添加而不是报错
    /// 这里虽然叫做 SKIP_IF_NULL 但实际上表示的有效参数数量是不是和所接受的参数数量一致
    /// 只是为了简化书写
    SKIP_IF_NULL,

    /// 在 in 或 not in 中 如果有效的参数条目 (指去除 null 后的) 为空 则跳过添加而不是报错
    /// 和 [#SKIP_IF_NULL] 相同 是为了简化书写 其实际意义为参数中去除非法数值(为 null)后的列表长度是否为 0
    SKIP_IF_EMPTY_LIST,

    //*************** 字段控制 **********************   

    /// 使用表达式 (不进行转换)
    USE_EXPRESSION,

    /// 使用 JSON 查询
    /// 注意和 {@link WhereType#JSON_CONTAINS} 一起使用时无效 因为 {@link WhereType#JSON_CONTAINS} 自己有针对 Json 的特殊实现
    USE_JSON_EXTRACT,

    /// 注意只适用于 JSON_CONTAINS
    /// JSON_CONTAINS 默认会将值转换为 JSON 并去除为 value 为 null 的 字段
    /// 使用 原始值 时会将值 直接传递到 SQL 语句
    /// 若值为 实体类 则会转换为 JSON 不过 和默认情况相比, 转换的 JSON 会包含 value 为 null 的字段
    USE_ORIGINAL_VALUE

}
