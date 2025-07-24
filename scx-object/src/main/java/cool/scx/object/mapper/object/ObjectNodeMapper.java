package cool.scx.object.mapper.object;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.Node;
import cool.scx.object.node.ObjectNode;
import cool.scx.reflect.AccessModifier;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.ConstructorInfo;
import cool.scx.reflect.FieldInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static cool.scx.object.node.NullNode.NULL;

/// 通用对象处理器
///
/// @author scx567888
/// @version 0.0.1
public final class ObjectNodeMapper implements NodeMapper<Object> {

    private final ClassInfo classInfo;
    private final ConstructorInfo defaultConstructor;
    private final FieldInfo[] readableFields;
    private final FieldInfo[] writableFields;

    public ObjectNodeMapper(ClassInfo classInfo) {
        this.classInfo = classInfo;
        // 这里我们只是获取一下这个 构造器, 并不进行 是否存在或是否可访问的校验, 
        // 因为有时候 ObjectNodeMapper 只会被用作 toNode 根本用不上 defaultConstructor, 所以这里延后校验以提供更强的容错性.
        this.defaultConstructor = this.classInfo.defaultConstructor();
        // 可读的字段, 这里只要 public 的实例字段
        this.readableFields = filterReadableFields(this.classInfo);
        // 可写的字段, 相较于可读 我们过滤掉 final 
        this.writableFields = filterWritableFields(this.readableFields);
    }

    private static FieldInfo[] filterReadableFields(ClassInfo classInfo) {
        // 因为 ObjectNodeMapper 每种类型的对象只会创建一次, 所以这里 使用 Stream 并没有什么性能问题
        // 注意我们这里需要连父级的字段也带上 
        return Arrays.stream(classInfo.allFields())
                .filter(c -> !c.isStatic() && c.accessModifier() == AccessModifier.PUBLIC)
                .peek(c -> c.setAccessible(true))// 处理一些类本身 就不是 public 的情况, 比如内部类
                .toArray(FieldInfo[]::new);
    }

    private static FieldInfo[] filterWritableFields(FieldInfo[] readableFields) {
        // 因为 ObjectNodeMapper 每种类型的对象只会创建一次, 所以这里 使用 Stream 并没有什么性能问题
        return Arrays.stream(readableFields).filter(c -> !c.isFinal()).toArray(FieldInfo[]::new);
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
        var objectNode = new ObjectNode();
        for (var fieldInfo : readableFields) {
            var name = fieldInfo.name();
            var value = getFieldValue(fieldInfo, objectValue);
            //处理忽略 null value
            if (value == null && context.options().ignoreNullValue()) {
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
        //3, 非 ObjectNode 类型无法转换直接报错
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
