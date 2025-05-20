package cool.scx.data.field_policy.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.field_policy.FilterMode;
import cool.scx.data.field_policy.QueryFieldPolicy;
import cool.scx.data.field_policy.QueryFieldPolicyImpl;

import java.util.Map;

import static cool.scx.common.util.ObjectUtils.convertValue;
import static cool.scx.data.field_policy.FilterMode.EXCLUDED;

/// FieldPolicyDeserializer
///
/// @author scx567888
/// @version 0.0.1
public class QueryFieldPolicyDeserializer {

    public static final QueryFieldPolicyDeserializer QUERY_FIELD_POLICY_DESERIALIZER = new QueryFieldPolicyDeserializer();

    public QueryFieldPolicy fromJson(String json) throws JsonProcessingException {
        var v = ObjectUtils.jsonMapper().readTree(json);
        return deserialize(v);
    }

    public QueryFieldPolicy deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            if (type.equals("QueryFieldPolicy")) {
                return deserializeFieldPolicy(v);
            }
        }
        throw new IllegalArgumentException("Invalid field policy: " + v);
    }

    private QueryFieldPolicy deserializeFieldPolicy(JsonNode objectNode) {
        var filterMode = EXCLUDED;

        if (objectNode == null) {
            return new QueryFieldPolicyImpl(filterMode);
        }

        var filterModeNode = objectNode.get("filterMode");
        var fieldNamesNode = objectNode.get("fieldNames");
        var virtualFieldsNode = objectNode.get("virtualFields");


        if (filterModeNode != null && !filterModeNode.isNull()) {
            filterMode = convertValue(filterModeNode, FilterMode.class);
        }

        var fieldPolicy = new QueryFieldPolicyImpl(filterMode);

        if (fieldNamesNode != null && !fieldNamesNode.isNull()) {
            var fieldNames = convertValue(fieldNamesNode, String[].class);
            fieldPolicy.addFieldNames(fieldNames);
        }

        if (virtualFieldsNode != null && !virtualFieldsNode.isNull()) {
            var expressions = convertValue(virtualFieldsNode, new TypeReference<Map<String, String>>() {});
            for (var entry : expressions.entrySet()) {
                fieldPolicy.virtualField(entry.getKey(), entry.getValue());
            }
        }

        return fieldPolicy;
    }

}
