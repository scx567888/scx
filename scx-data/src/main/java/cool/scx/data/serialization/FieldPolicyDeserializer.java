package cool.scx.data.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.util.ObjectUtils;
import cool.scx.data.field_policy.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cool.scx.common.util.ObjectUtils.convertValue;

/// FieldPolicyDeserializer
///
/// @author scx567888
/// @version 0.0.1
public class FieldPolicyDeserializer {

    public static FieldPolicy deserializeFieldPolicyFromJson(String json) throws DeserializationException {
        try {
            var v = ObjectUtils.jsonMapper().readTree(json);
            return deserializeFieldPolicy(v);
        } catch (JsonProcessingException e) {
            throw new DeserializationException(e);
        }
    }

    public static FieldPolicy deserializeFieldPolicy(JsonNode v) throws DeserializationException {
        if (v == null || v.isNull()) {
            throw new DeserializationException("FieldPolicy node is null");
        }
        if (!v.isObject()) {
            throw new DeserializationException("FieldPolicy node is not an object: " + v);
        }
        var typeNode = v.get("@type");
        if (typeNode == null || !"FieldPolicy".equals(typeNode.asText())) {
            throw new DeserializationException("Unknown or missing @type for FieldPolicy: " + v);
        }

        //filterMode 不允许为空
        var filterModeNode = v.get("filterMode");

        if (filterModeNode == null || filterModeNode.isNull()) {
            throw new DeserializationException("FilterMode node is null");
        }

        var filterMode = convertValue(filterModeNode, FilterMode.class);

        // 创建一个 FieldPolicy 实例
        var policy = new FieldPolicyImpl(filterMode);

        var fieldNamesNode = v.get("fieldNames");

        if (fieldNamesNode == null || !fieldNamesNode.isArray()) {
            throw new DeserializationException("FieldNames node is null or not an array: " + v);
        }

        List<String> fieldNames = new ArrayList<>();
        for (var fn : fieldNamesNode) {
            fieldNames.add(fn.asText());
        }
        policy.addFieldNames(fieldNames.toArray(String[]::new));


        var virtualFieldsNode = v.get("virtualFields");
        if (virtualFieldsNode == null || !virtualFieldsNode.isArray()) {
            throw new DeserializationException("VirtualFields node is null or not an array: " + v);
        }

        var virtualFields = new ArrayList<VirtualField>();
        for (var jsonNode : virtualFieldsNode) {
            virtualFields.add(deserializeVirtualField(jsonNode));
        }
        policy.virtualFields(virtualFields.toArray(VirtualField[]::new));

        var assignFieldsNode = v.get("assignFields");
        if (assignFieldsNode == null || !assignFieldsNode.isArray()) {
            throw new DeserializationException("AssignFields node is null or not an array: " + v);
        }
        var assignFields = new ArrayList<AssignField>();
        for (var jsonNode : assignFieldsNode) {
            assignFields.add(deserializeAssignField(jsonNode));
        }
        policy.assignFields(assignFields.toArray(AssignField[]::new));


        var ignoreNullNode = v.get("ignoreNull");
        if (ignoreNullNode == null || ignoreNullNode.isNull()) {
            throw new DeserializationException("IgnoreNull node is null: " + v);
        }
        var ignoreNull = ignoreNullNode.asBoolean(true);
        policy.ignoreNull(ignoreNull);


        var ignoreNullsNode = v.get("ignoreNulls");
        if (ignoreNullsNode == null || !ignoreNullsNode.isObject()) {
            throw new DeserializationException("IgnoreNulls node is null or not an map: " + v);
        }

        var ignoreNulls = convertValue(ignoreNullsNode, new TypeReference<Map<String, Boolean>>() {});

        for (var e : ignoreNulls.entrySet()) {
            policy.ignoreNull(e.getKey(), e.getValue());
        }

        return policy;
    }

    private static VirtualField deserializeVirtualField(JsonNode node) throws DeserializationException {
        if (node == null || node.isNull()) {
            throw new DeserializationException("VirtualField node is null");
        }
        //检查类型
        var typeNode = node.get("@type");
        if (typeNode == null || !"VirtualField".equals(typeNode.asText())) {
            throw new DeserializationException("Unknown or missing @type for VirtualField: " + node);
        }
        //这两个都不允许为空
        var virtualFieldNameNode = node.get("virtualFieldName");
        var expressionNode = node.get("expression");
        if (virtualFieldNameNode == null || !virtualFieldNameNode.isTextual()) {
            throw new DeserializationException("VirtualFieldName node is null or not a string: " + node);
        }
        if (expressionNode == null || !expressionNode.isTextual()) {
            throw new DeserializationException("Expression node is null or not a string: " + node);
        }
        var virtualFieldName = virtualFieldNameNode.asText();
        var expression = expressionNode.asText();
        return new VirtualField(virtualFieldName, expression);
    }

    private static AssignField deserializeAssignField(JsonNode node) throws DeserializationException {
        if (node == null || node.isNull()) {
            throw new DeserializationException("AssignField node is null");
        }
        //检查类型
        var typeNode = node.get("@type");
        if (typeNode == null || !"AssignField".equals(typeNode.asText())) {
            throw new DeserializationException("Unknown or missing @type for AssignField: " + node);
        }
        //这两个也不允许为空
        var fieldNameNode = node.get("fieldName");
        var expressionNode = node.get("expression");
        if (fieldNameNode == null || !fieldNameNode.isTextual()) {
            throw new DeserializationException("AssignFieldName node is null or not a string: " + node);
        }
        if (expressionNode == null || !expressionNode.isTextual()) {
            throw new DeserializationException("Expression node is null or not a string: " + node);
        }
        var fieldName = fieldNameNode.asText();
        var expression = expressionNode.asText();
        return new AssignField(fieldName, expression);
    }

}
