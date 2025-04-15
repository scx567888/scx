package cool.scx.data.field_policy;

/// 字段表达式
/// 可用于查询 age -> age * 2
/// 可用于更新 age = age + 1
public record FieldExpression(String fieldName, String expression) {

}
