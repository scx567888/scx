package cool.scx.data.field_policy.serializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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

        if (objectNode.get("filterMode") != null && !objectNode.get("filterMode").isNull()) {
            filterMode = convertValue(objectNode.get("filterMode"), FilterMode.class);
        }

        var fieldPolicy = new FieldPolicyImpl(filterMode);

        if (objectNode.get("fieldNames") != null && !objectNode.get("fieldNames").isNull()) {
            var fieldNames = convertValue(objectNode.get("fieldNames"), String[].class);
            fieldPolicy.addFieldNames(fieldNames);
        }

        if (objectNode.get("ignoreNull") != null && !objectNode.get("ignoreNull").isNull()) {
            var ignoreNull = objectNode.get("ignoreNull").asBoolean();
            fieldPolicy.ignoreNull(ignoreNull);
        }

        if (objectNode.get("ignoreNulls") != null && !objectNode.get("ignoreNulls").isNull()) {
            var ignoreNulls = convertValue(objectNode.get("ignoreNulls"), new TypeReference<Map<String, Boolean>>() {});
            for (var entry : ignoreNulls.entrySet()) {
                fieldPolicy.ignoreNull(entry.getKey(), entry.getValue());
            }
        }

        if (objectNode.get("expressions") != null && !objectNode.get("expressions").isNull()) {
            var expressions = convertValue(objectNode.get("expressions"), new TypeReference<Map<String, String>>() {});
            for (var entry : expressions.entrySet()) {
                fieldPolicy.expression(entry.getKey(), entry.getValue());
            }
        }

        return fieldPolicy;
    }

}
