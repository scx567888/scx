package cool.scx.data.field_policy.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.field_policy.QueryFieldPolicy;
import cool.scx.data.field_policy.VirtualField;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/// FieldPolicySerializer
///
/// @author scx567888
/// @version 0.0.1
public class QueryFieldPolicySerializer {

    public static final QueryFieldPolicySerializer QUERY_FIELD_POLICY_SERIALIZER = new QueryFieldPolicySerializer();

    public Map<String, Object> serializeVirtualField(VirtualField args) {
        var s = new LinkedHashMap<String, Object>();
        s.put("@type", "VirtualField");
        s.put("expression", args.expression());
        s.put("virtualFieldName", args.virtualFieldName());
        return s;
    }

    public String toJson(QueryFieldPolicy fieldPolicy) throws JsonProcessingException {
        var v = serialize(fieldPolicy);
        return ObjectUtils.jsonMapper().writeValueAsString(v);
    }

    public Map<String, Object> serialize(QueryFieldPolicy fieldPolicy) {
        return serializeQueryFieldPolicy(fieldPolicy);
    }

    private Map<String, Object> serializeQueryFieldPolicy(QueryFieldPolicy fieldPolicy) {
        var m = new LinkedHashMap<String, Object>();
        m.put("@type", "QueryFieldPolicy");
        m.put("filterMode", fieldPolicy.getFilterMode());
        m.put("fieldNames", fieldPolicy.getFieldNames());
        m.put("virtualFields", serializeVirtualFields(fieldPolicy.getVirtualFields()));
        return m;
    }

    public ArrayList<Object> serializeVirtualFields(VirtualField... virtualFields) {
        var s = new ArrayList<>();
        for (VirtualField virtualField : virtualFields) {
            s.add(serializeVirtualField(virtualField));
        }
        return s;
    }


}
