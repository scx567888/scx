package cool.scx.data.field_policy;

import static cool.scx.data.field_policy.FilterMode.EXCLUDED;
import static cool.scx.data.field_policy.FilterMode.INCLUDED;

/// FieldFilterBuilder
///
/// @author scx567888
/// @version 0.0.1
public class FieldPolicyBuilder {

    public static FieldPolicy includedAll() {
        return new FieldPolicyImpl(EXCLUDED);
    }

    public static FieldPolicy excludedAll() {
        return new FieldPolicyImpl(INCLUDED);
    }

    public static FieldPolicy included(String... fieldNames) {
        return excludedAll().included(fieldNames);
    }

    public static FieldPolicy excluded(String... fieldNames) {
        return includedAll().excluded(fieldNames);
    }

    /// 默认包含所有
    public static FieldPolicy ignoreNullValue(boolean ignoreNullValue) {
        return includedAll().ignoreNullValue(ignoreNullValue);
    }

    /// 默认排除所有
    public static FieldPolicy fieldExpression(String fieldName, String expression) {
        return excludedAll().fieldExpression(fieldName, expression);
    }

}
