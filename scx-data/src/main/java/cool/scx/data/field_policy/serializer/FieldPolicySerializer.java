package cool.scx.data.field_policy.serializer;

import cool.scx.data.field_policy.FieldPolicy;

import java.util.LinkedHashMap;

/// FieldPolicySerializer
///
/// @author scx567888
/// @version 0.0.1
public class FieldPolicySerializer {

    public static final FieldPolicySerializer FIELD_POLICY_SERIALIZER = new FieldPolicySerializer();

    public Object serialize(Object obj) {
        return switch (obj) {
            case FieldPolicy s -> serializeFieldPolicy(s);
            default -> obj;
        };
    }

    public LinkedHashMap<String, Object> serializeFieldPolicy(FieldPolicy fieldPolicy) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "FieldPolicy");
        m.put("filterMode", fieldPolicy.filterMode());
        m.put("fieldNames", fieldPolicy.fieldNames());
        m.put("ignoreNull", fieldPolicy.ignoreNull());
        m.put("ignoreNulls", fieldPolicy.ignoreNulls());
        m.put("expressions", fieldPolicy.expressions());
        return m;
    }

}
