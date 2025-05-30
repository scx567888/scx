package cool.scx.data.serialization;

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

    public static String serializeFieldPolicyToJson(FieldPolicy fieldPolicy) throws SerializationException {
        var m = serializeFieldPolicy(fieldPolicy);
        try {
            return ObjectUtils.jsonMapper().writeValueAsString(m);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }

    public static Map<String, Object> serializeFieldPolicy(FieldPolicy fieldPolicy) {
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

    private static Map<String, Object> serializeVirtualField(VirtualField virtualField) {
        var s = new LinkedHashMap<String, Object>();
        s.put("@type", "VirtualField");
        s.put("expression", virtualField.expression());
        s.put("virtualFieldName", virtualField.virtualFieldName());
        return s;
    }

    private static Map<String, Object> serializeAssignField(AssignField assignField) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "AssignField");
        m.put("fieldName", assignField.fieldName());
        m.put("expression", assignField.expression());
        return m;
    }

    private static ArrayList<Object> serializeVirtualFields(VirtualField[] virtualFields) {
        var s = new ArrayList<>();
        for (var virtualField : virtualFields) {
            s.add(serializeVirtualField(virtualField));
        }
        return s;
    }

    private static ArrayList<Object> serializeAssignFields(AssignField[] assignFields) {
        var s = new ArrayList<>();
        for (var assignField : assignFields) {
            s.add(serializeAssignField(assignField));
        }
        return s;
    }

}
