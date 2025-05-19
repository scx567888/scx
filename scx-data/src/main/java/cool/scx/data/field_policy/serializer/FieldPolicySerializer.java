package cool.scx.data.field_policy.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.field_policy.FieldPolicy;

import java.util.LinkedHashMap;

/// FieldPolicySerializer
///
/// @author scx567888
/// @version 0.0.1
public class FieldPolicySerializer {

    public static final FieldPolicySerializer FIELD_POLICY_SERIALIZER = new FieldPolicySerializer();

    public String toJson(FieldPolicy fieldPolicy) throws JsonProcessingException {
        var v = serialize(fieldPolicy);
        return ObjectUtils.jsonMapper().writeValueAsString(v);
    }

    public Object serialize(FieldPolicy fieldPolicy) {
        return serializeFieldPolicy(fieldPolicy);
    }

    private LinkedHashMap<String, Object> serializeFieldPolicy(FieldPolicy fieldPolicy) {
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
