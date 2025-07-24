package cool.scx.data.serialization;

import cool.scx.data.field_policy.*;
import cool.scx.object.ScxObject;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ObjectNode;
import cool.scx.object.node.ValueNode;
import cool.scx.reflect.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cool.scx.object.ScxObject.convertValue;

public class FieldPolicyDeserializer {

    public static FieldPolicy deserializeFieldPolicyFromJson(String json) throws DeserializationException {
        try {
            var node = ScxObject.fromJson(json);
            return deserializeFieldPolicy(node);
        } catch (Exception e) {
            throw new DeserializationException(e);
        }
    }

    public static FieldPolicy deserializeFieldPolicy(Node v) throws DeserializationException {
        if (v == null || v.isNull() || !(v instanceof ObjectNode objNode)) {
            throw new DeserializationException("FieldPolicy node is null or not an object");
        }

        var typeNode = objNode.get("@type");
        if (!(typeNode instanceof ValueNode vn) || !"FieldPolicy".equals(vn.asText())) {
            throw new DeserializationException("Unknown or missing @type for FieldPolicy");
        }

        var filterModeNode = objNode.get("filterMode");
        if (!(filterModeNode instanceof Node)) {
            throw new DeserializationException("filterMode is missing or null");
        }
        var filterMode = convertValue(filterModeNode, FilterMode.class);

        var policy = new FieldPolicyImpl(filterMode);

        var fieldNamesNode = objNode.get("fieldNames");
        if (!(fieldNamesNode instanceof ArrayNode arrayNode)) {
            throw new DeserializationException("fieldNames is null or not an array");
        }

        List<String> fieldNames = new ArrayList<>();
        for (Node fn : arrayNode) {
            if (!(fn instanceof ValueNode vNode)) {
                throw new DeserializationException("Each fieldName must be a ValueNode");
            }
            fieldNames.add(vNode.asText());
        }

        if (filterMode == FilterMode.INCLUDED) {
            policy.include(fieldNames.toArray(String[]::new));
        } else if (filterMode == FilterMode.EXCLUDED) {
            policy.exclude(fieldNames.toArray(String[]::new));
        }

        var virtualFieldsNode = objNode.get("virtualFields");
        if (!(virtualFieldsNode instanceof ArrayNode vfArray)) {
            throw new DeserializationException("virtualFields is null or not an array");
        }

        var virtualFields = new ArrayList<VirtualField>();
        for (Node vfNode : vfArray) {
            virtualFields.add(deserializeVirtualField(vfNode));
        }
        policy.virtualFields(virtualFields.toArray(VirtualField[]::new));

        var assignFieldsNode = objNode.get("assignFields");
        if (!(assignFieldsNode instanceof ArrayNode afArray)) {
            throw new DeserializationException("assignFields is null or not an array");
        }

        var assignFields = new ArrayList<AssignField>();
        for (Node afNode : afArray) {
            assignFields.add(deserializeAssignField(afNode));
        }
        policy.assignFields(assignFields.toArray(AssignField[]::new));

        var ignoreNullNode = objNode.get("ignoreNull");
        if (!(ignoreNullNode instanceof ValueNode ignoreVN)) {
            throw new DeserializationException("ignoreNull is missing or invalid");
        }
        policy.ignoreNull(ignoreVN.asBoolean());

        var ignoreNullsNode = objNode.get("ignoreNulls");
        if (!(ignoreNullsNode instanceof ObjectNode ignoreMapNode)) {
            throw new DeserializationException("ignoreNulls is not an object");
        }

        Map<String, Boolean> ignoreNulls = convertValue(ignoreMapNode, new TypeReference<>() {});
        for (var e : ignoreNulls.entrySet()) {
            policy.ignoreNull(e.getKey(), e.getValue());
        }

        return policy;
    }

    private static VirtualField deserializeVirtualField(Node node) throws DeserializationException {
        if (node == null || node.isNull() || !(node instanceof ObjectNode objNode)) {
            throw new DeserializationException("VirtualField must be an ObjectNode");
        }

        var typeNode = objNode.get("@type");
        if (!(typeNode instanceof ValueNode vn) || !"VirtualField".equals(vn.asText())) {
            throw new DeserializationException("Invalid or missing @type for VirtualField");
        }

        var nameNode = objNode.get("virtualFieldName");
        var exprNode = objNode.get("expression");

        if (!(nameNode instanceof ValueNode nameVN) || !(exprNode instanceof ValueNode exprVN)) {
            throw new DeserializationException("virtualFieldName or expression is invalid in VirtualField");
        }

        return new VirtualField(nameVN.asText(), exprVN.asText());
    }

    private static AssignField deserializeAssignField(Node node) throws DeserializationException {
        if (node == null || node.isNull() || !(node instanceof ObjectNode objNode)) {
            throw new DeserializationException("AssignField must be an ObjectNode");
        }

        var typeNode = objNode.get("@type");
        if (!(typeNode instanceof ValueNode vn) || !"AssignField".equals(vn.asText())) {
            throw new DeserializationException("Invalid or missing @type for AssignField");
        }

        var fieldNameNode = objNode.get("fieldName");
        var exprNode = objNode.get("expression");

        if (!(fieldNameNode instanceof ValueNode fnVN) || !(exprNode instanceof ValueNode exprVN)) {
            throw new DeserializationException("fieldName or expression is invalid in AssignField");
        }

        return new AssignField(fnVN.asText(), exprVN.asText());
    }

}
