package cool.scx.object.mapping.mapper.bean;

import cool.scx.object.mapping.FromNodeContext;
import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.mapping.ToNodeContext;
import cool.scx.object.node.ArrayNode;
import cool.scx.object.node.Node;
import cool.scx.object.node.ObjectNode;
import cool.scx.reflect.AccessModifier;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.ConstructorInfo;
import cool.scx.reflect.FieldInfo;

import java.lang.reflect.InvocationTargetException;

import static cool.scx.object.node.NullNode.NULL;

/// 通用对象处理器
///
/// 支持 单值数组解包
///
/// @author scx567888
/// @version 0.0.1
public final class BeanNodeMapper implements NodeMapper<Object> {

    private static final BeanNodeMapperOptions BEAN_NODE_MAPPER_OPTIONS = new BeanNodeMapperOptions();

    private final ClassInfo classInfo;
    private final ConstructorInfo defaultConstructor;
    private final FieldInfo[] readableFields;
    private final FieldInfo[] writableFields;

    public BeanNodeMapper(ClassInfo classInfo, ConstructorInfo defaultConstructor, FieldInfo[] readableFields, FieldInfo[] writableFields) {
        this.classInfo = classInfo;
        this.defaultConstructor = defaultConstructor;
        this.readableFields = readableFields;
        this.writableFields = writableFields;
    }

    private static Object getFieldValue(FieldInfo fieldInfo, Object value) throws NodeMappingException {
        try {
            return fieldInfo.get(value);
        } catch (IllegalAccessException e) {
            // 因为我们 使用的都是 public 字段 理论上不会出现 这种异常
            throw new NodeMappingException(e);
        }
    }

    private static void setFieldValue(FieldInfo fieldInfo, Object object, Object value) throws NodeMappingException {
        try {
            fieldInfo.set(object, value);
        } catch (IllegalAccessException e) {
            // 因为我们 使用的都是 public 字段 理论上不会出现 这种异常
            throw new NodeMappingException(e);
        }
    }

    @Override
    public Node toNode(Object objectValue, ToNodeContext context) throws NodeMappingException {
        var options = context.options().getMapperOptions(BeanNodeMapperOptions.class, BEAN_NODE_MAPPER_OPTIONS);
        var objectNode = new ObjectNode();
        for (var fieldInfo : readableFields) {
            var name = fieldInfo.name();
            var value = getFieldValue(fieldInfo, objectValue);
            //处理忽略 null value
            if (value == null && options.ignoreNullValue()) {
                continue;
            }
            if (options.needIgnore(classInfo, fieldInfo)) {
                continue;
            }
            objectNode.put(name, context.toNode(value, name));
        }
        return objectNode;
    }

    @Override
    public Object fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理 ObjectNode 类型
        if (node instanceof ObjectNode objectNode) {
            var object = newInstance();
            for (var fieldInfo : writableFields) {
                var tempNode = objectNode.get(fieldInfo.name());
                //这里不要把 null 传递到 fromNode 中防止引发错误
                if (tempNode == null) {
                    tempNode = NULL;
                }
                var v = context.fromNode(tempNode, fieldInfo.fieldType());
                setFieldValue(fieldInfo, object, v);
            }
            return object;
        }
        //3, 尝试处理 单值数组 (这里假设 ArrayNode 不存在自引用)
        if (node instanceof ArrayNode arrayNode && arrayNode.size() == 1) {
            return this.fromNode(arrayNode.get(0), context);
        }
        //4, 非 ObjectNode 类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

    public Object newInstance() throws NodeMappingException {
        if (defaultConstructor == null) {
            throw new NodeMappingException("未找到可用的构造函数: " + classInfo);
        }
        if (defaultConstructor.accessModifier() != AccessModifier.PUBLIC) {
            throw new NodeMappingException("未找到可用的 public 构造函数: " + classInfo);
        }
        try {
            return defaultConstructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            // 因为我们使用的都是 public 构造函数, 所以这里理论上不会出现异常, 除非构造函数方法内部逻辑异常
            throw new NodeMappingException(e);
        }
    }

}
