package cool.scx.data.field_policy.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.field_policy.*;

import java.util.ArrayList;
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
        var virtualFieldsNode = objectNode.get("virtualFields");
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

        if (virtualFieldsNode != null && !virtualFieldsNode.isNull()) {
            var a = new ArrayList<VirtualField>();
            for (var jsonNode : virtualFieldsNode) {
                a.add(deserializeVirtualField(jsonNode));
            }
            fieldPolicy.virtualFields(a.toArray(VirtualField[]::new));
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
            var a = new ArrayList<Expression>();
            for (var jsonNode : expressionsNode) {
                a.add(deserializeExpression(jsonNode));
            }
            fieldPolicy.expressions(a.toArray(Expression[]::new));
        }

        return fieldPolicy;

    }

    public VirtualField deserializeVirtualField(JsonNode v) {
        var expressionNode = v.get("expression");
        var virtualFieldNameNode = v.get("virtualFieldName");
        return new VirtualField(expressionNode.asText(), virtualFieldNameNode.asText());
    }

    public Expression deserializeExpression(JsonNode v) {
        var fieldNameNode = v.get("fieldName");
        var expressionNode = v.get("expression");
        return new Expression(fieldNameNode.asText(), expressionNode.asText());
    }

}
