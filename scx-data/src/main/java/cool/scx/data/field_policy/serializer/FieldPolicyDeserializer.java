package cool.scx.data.field_policy.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.field_policy.FieldPolicyImpl;
import cool.scx.data.field_policy.FilterMode;

import java.util.Map;

import static cool.scx.common.util.ObjectUtils.convertValue;
import static cool.scx.data.field_policy.FilterMode.EXCLUDED;

/// FieldPolicyDeserializer
///
/// @author scx567888
/// @version 0.0.1
public class FieldPolicyDeserializer {

    public static final FieldPolicyDeserializer FIELD_POLICY_DESERIALIZER = new FieldPolicyDeserializer();

    public FieldPolicy fromJson(String json) throws JsonProcessingException {
        var v = ObjectUtils.jsonMapper().readTree(json);
        return deserialize(v);
    }

    public FieldPolicy deserialize(JsonNode v) {
        if (v.isObject()) {
            var type = v.get("@type").asText();
            if (type.equals("FieldPolicy")) {
                return deserializeFieldPolicy(v);
            }
        }
        throw new IllegalArgumentException("Invalid field policy: " + v);
    }

    private FieldPolicy deserializeFieldPolicy(JsonNode objectNode) {
        var filterMode = EXCLUDED;

        if (objectNode == null) {
            return new FieldPolicyImpl(filterMode);
        }

        var filterModeNode = objectNode.get("filterMode");
        var fieldNamesNode = objectNode.get("fieldNames");
        var ignoreNullNode = objectNode.get("ignoreNull");
        var ignoreNullsNode = objectNode.get("ignoreNulls");
        var expressionsNode = objectNode.get("expressions");


        if (filterModeNode != null && !filterModeNode.isNull()) {
            filterMode = convertValue(filterModeNode, FilterMode.class);
        }

        var fieldPolicy = new FieldPolicyImpl(filterMode);

        if (fieldNamesNode != null && !fieldNamesNode.isNull()) {
            var fieldNames = convertValue(fieldNamesNode, String[].class);
            fieldPolicy.addFieldNames(fieldNames);
        }

        if (ignoreNullNode != null && !ignoreNullNode.isNull()) {
            var ignoreNull = ignoreNullNode.asBoolean();
            fieldPolicy.ignoreNull(ignoreNull);
        }

        if (ignoreNullsNode != null && !ignoreNullsNode.isNull()) {
            var ignoreNulls = convertValue(ignoreNullsNode, new TypeReference<Map<String, Boolean>>() {});
            for (var entry : ignoreNulls.entrySet()) {
                fieldPolicy.ignoreNull(entry.getKey(), entry.getValue());
            }
        }

        if (expressionsNode != null && !expressionsNode.isNull()) {
            var expressions = convertValue(expressionsNode, new TypeReference<Map<String, String>>() {});
            for (var entry : expressions.entrySet()) {
                fieldPolicy.expression(entry.getKey(), entry.getValue());
            }
        }

        return fieldPolicy;
    }

}
