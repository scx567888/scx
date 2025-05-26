package cool.scx.data.field_policy.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.field_policy.AssignField;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.field_policy.VirtualField;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public Map<String, Object> serializeVirtualField(VirtualField args) {
        var s = new LinkedHashMap<String, Object>();
        s.put("@type", "VirtualField");
        s.put("expression", args.expression());
        s.put("virtualFieldName", args.virtualFieldName());
        return s;
    }

    public Map<String, Object> serialize(FieldPolicy fieldPolicy) {
        return serializeFieldPolicy(fieldPolicy);
    }

    private Map<String, Object> serializeFieldPolicy(FieldPolicy fieldPolicy) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "FieldPolicy");
        m.put("filterMode", fieldPolicy.getFilterMode());
        m.put("fieldNames", fieldPolicy.getFieldNames());
        m.put("virtualFields", serializeVirtualFields(fieldPolicy.getVirtualFields()));
        m.put("ignoreNull", fieldPolicy.getIgnoreNull());
        m.put("ignoreNulls", fieldPolicy.getIgnoreNulls());
        m.put("assignFields", serializeAssignFields(fieldPolicy.getAssignFields()));
        return m;
    }

    public ArrayList<Object> serializeVirtualFields(VirtualField... virtualFields) {
        var s = new ArrayList<>();
        for (VirtualField virtualField : virtualFields) {
            s.add(serializeVirtualField(virtualField));
        }
        return s;
    }

    public ArrayList<Object> serializeAssignFields(AssignField... expressions) {
        var s = new ArrayList<>();
        for (var expression : expressions) {
            s.add(serializeAssignField(expression));
        }
        return s;
    }

    public Map<String, Object> serializeAssignField(AssignField expression) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "AssignField");
        m.put("fieldName", expression.fieldName());
        m.put("expression", expression.expression());
        return m;
    }

}
