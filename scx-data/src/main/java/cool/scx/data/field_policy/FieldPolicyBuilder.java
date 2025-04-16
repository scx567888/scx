package cool.scx.data.field_policy;

import static cool.scx.data.field_policy.FilterMode.EXCLUDED;
import static cool.scx.data.field_policy.FilterMode.INCLUDED;

/// FieldFilterBuilder
///
/// @author scx567888
/// @version 0.0.1
public class FieldPolicyBuilder {

    /// 白名单模式
    public static FieldPolicy ofIncluded(String... fieldNames) {
        return new FieldPolicyImpl(INCLUDED).addIncluded(fieldNames);
    }

    /// 黑名单模式
    public static FieldPolicy ofExcluded(String... fieldNames) {
        return new FieldPolicyImpl(EXCLUDED).addExcluded(fieldNames);
    }

    /// 表达式
    public static FieldPolicy ofFieldExpression(FieldExpression... fieldExpressions) {
        return new FieldPolicyImpl(EXCLUDED).addFieldExpression(fieldExpressions);
    }

    /// 表达式
    public static FieldPolicy ofFieldExpression(String fieldName, String expression) {
        return new FieldPolicyImpl(EXCLUDED).addFieldExpression(fieldName, expression);
    }

}
