package cool.scx.object.mapper;

import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.object.node.ObjectNode;
import cool.scx.reflect.AccessModifier;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.FieldInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/// 通用对象处理器
public class ObjectNodeMapper implements NodeMapper<Object> {

    private final ClassInfo classInfo;
    private final FieldInfo[] fields;

    public ObjectNodeMapper(ClassInfo classInfo) {
        this.classInfo = classInfo;
        this.fields = initFields(this.classInfo);
    }

    /// 提取 非静态的 public 字段
    public static FieldInfo[] initFields(ClassInfo classInfo) {
        var fieldList = new ArrayList<FieldInfo>();
        for (var fieldInfo : classInfo.allFields()) {
            if (fieldInfo.accessModifier() == AccessModifier.PUBLIC && !fieldInfo.isStatic()) {
                fieldList.add(fieldInfo);
            }
        }
        return fieldList.toArray(FieldInfo[]::new);
    }

    public static Object getFieldValue(FieldInfo fieldInfo, Object value) {
        try {
            return fieldInfo.get(value);
        } catch (IllegalAccessException e) {
            // 因为我们 使用的都是 public 字段 理论上不会出现 这种异常
            throw new IllegalArgumentException("");
        }
    }

    public static void setFieldValue(FieldInfo fieldInfo, Object object, Object value) {
        try {
            fieldInfo.set(object, value);
        } catch (IllegalAccessException e) {
            // 因为我们 使用的都是 public 字段 理论上不会出现 这种异常
            throw new IllegalArgumentException("");
        }
    }

    public Object newInstance() {
        try {
            return classInfo.defaultConstructor().newInstance();
        } catch (IllegalAccessException e) {
            // 因为我们 使用的都是 public 字段 理论上不会出现 这种异常
            throw new IllegalArgumentException("");
        } catch (InvocationTargetException | InstantiationException e) {
            throw new IllegalArgumentException("");
        }
    }

    @Override
    public Node toNode(Object value, NodeMapperSelector selector) {
        var objectNode = new ObjectNode();
        for (var fieldInfo : fields) {
            var fieldName = fieldInfo.name();
            var fieldValue = getFieldValue(fieldInfo, value);
            var childNode = selector.toNode(fieldValue);
            objectNode.put(fieldName, childNode);
        }
        return objectNode;
    }

    @Override
    public Object fromNode(Node node, NodeMapperSelector selector) {
        if (node == NullNode.NULL) {
            return null;
        }
        if (node instanceof ObjectNode objectNode) {
            var object = newInstance();
            for (var fieldInfo : fields) {
                var fieldName = fieldInfo.name();
                var nodeValue = objectNode.get(fieldName);
                var childValue = selector.fromNode(nodeValue, fieldInfo.fieldType());
                setFieldValue(fieldInfo, object, childValue);
            }
            return object;
        }
        throw new IllegalArgumentException("node type not supported : " + node.getClass());
    }

}
