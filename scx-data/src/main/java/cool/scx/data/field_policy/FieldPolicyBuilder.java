package cool.scx.data.field_policy;

import static cool.scx.data.field_policy.FilterMode.EXCLUDED;
import static cool.scx.data.field_policy.FilterMode.INCLUDED;

/// FieldPolicyBuilder
///
/// @author scx567888
/// @version 0.0.1
public class FieldPolicyBuilder {

    public static QueryFieldPolicy queryIncludeAll() {
        return new QueryFieldPolicyImpl(EXCLUDED);
    }

    public static QueryFieldPolicy queryExcludeAll() {
        return new QueryFieldPolicyImpl(INCLUDED) {};
    }

    public static QueryFieldPolicy queryInclude(String... fieldNames) {
        return queryExcludeAll().include(fieldNames);
    }

    public static QueryFieldPolicy queryExclude(String... fieldNames) {
        return queryIncludeAll().exclude(fieldNames);
    }

    public static UpdateFieldPolicy updateIncludeAll() {
        return new UpdateFieldPolicyImpl(EXCLUDED);
    }

    public static UpdateFieldPolicy updateExcludeAll() {
        return new UpdateFieldPolicyImpl(INCLUDED);
    }

    public static UpdateFieldPolicy updateInclude(String... fieldNames) {
        return updateExcludeAll().include(fieldNames);
    }

    public static UpdateFieldPolicy updateExclude(String... fieldNames) {
        return updateIncludeAll().exclude(fieldNames);
    }

    /// 默认包含所有
    public static UpdateFieldPolicy ignoreNull(boolean ignoreNull) {
        return updateIncludeAll().ignoreNull(ignoreNull);
    }

    /// 默认包含所有
    public static UpdateFieldPolicy ignoreNull(String fieldName, boolean ignoreNull) {
        return updateIncludeAll().ignoreNull(fieldName, ignoreNull);
    }

    /// 默认包含所有
    public static UpdateFieldPolicy expression(String fieldName, String expression) {
        return updateIncludeAll().expression(fieldName, expression);
    }

    public static QueryFieldPolicy virtualField(String expression, String virtualFieldName) {
        return queryIncludeAll().virtualField(expression, virtualFieldName);
    }
    
    public static QueryFieldPolicy virtualField(String expression) {
        return queryIncludeAll().virtualField(expression);
    }

}
