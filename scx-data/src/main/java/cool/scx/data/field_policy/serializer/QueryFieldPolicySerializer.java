package cool.scx.data.field_policy.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.field_policy.QueryFieldPolicy;

import java.util.LinkedHashMap;
import java.util.Map;

/// FieldPolicySerializer
///
/// @author scx567888
/// @version 0.0.1
public class QueryFieldPolicySerializer {

    public static final QueryFieldPolicySerializer QUERY_FIELD_POLICY_SERIALIZER = new QueryFieldPolicySerializer();

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
        m.put("virtualFields", fieldPolicy.getVirtualFields());
        return m;
    }

}
