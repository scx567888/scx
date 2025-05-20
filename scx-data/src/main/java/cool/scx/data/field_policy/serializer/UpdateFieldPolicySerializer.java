package cool.scx.data.field_policy.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.field_policy.QueryFieldPolicy;
import cool.scx.data.field_policy.UpdateFieldPolicy;

import java.util.LinkedHashMap;
import java.util.Map;

/// FieldPolicySerializer
///
/// @author scx567888
/// @version 0.0.1
public class UpdateFieldPolicySerializer {

    public static final UpdateFieldPolicySerializer UPDATE_FIELD_POLICY_SERIALIZER = new UpdateFieldPolicySerializer();

    public String toJson(UpdateFieldPolicy fieldPolicy) throws JsonProcessingException {
        var v = serialize(fieldPolicy);
        return ObjectUtils.jsonMapper().writeValueAsString(v);
    }

    public Map<String, Object> serialize(UpdateFieldPolicy fieldPolicy) {
        return serializeUpdateFieldPolicy(fieldPolicy);
    }

    private Map<String, Object> serializeUpdateFieldPolicy(UpdateFieldPolicy fieldPolicy) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "UpdateFieldPolicy");
        m.put("filterMode", fieldPolicy.getFilterMode());
        m.put("fieldNames", fieldPolicy.getFieldNames());
        m.put("ignoreNull", fieldPolicy.getIgnoreNull());
        m.put("ignoreNulls", fieldPolicy.getIgnoreNulls());
        m.put("expressions", fieldPolicy.getExpressions());
        return m;
    }

}
