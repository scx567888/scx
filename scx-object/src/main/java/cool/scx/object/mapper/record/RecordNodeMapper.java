package cool.scx.object.mapper.record;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.Node;
import cool.scx.object.node.ObjectNode;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.ConstructorInfo;
import cool.scx.reflect.ParameterInfo;
import cool.scx.reflect.RecordComponentInfo;

import java.lang.reflect.InvocationTargetException;

import static cool.scx.object.node.NullNode.NULL;

/// RecordNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class RecordNodeMapper implements NodeMapper<Record> {

    private final ClassInfo classInfo;
    private final RecordComponentInfo[] recordComponents;
    private final ConstructorInfo recordConstructor;
    private final ParameterInfo[] parameters;

    public RecordNodeMapper(ClassInfo classInfo) {
        this.classInfo = classInfo;
        //因为 ClassInfo.recordComponents() 每次都会 clone 一次, 这里为了性能单独存一份, 其余同理.
        this.recordComponents = this.classInfo.recordComponents();
        this.recordConstructor = this.classInfo.recordConstructor();
        this.parameters = this.recordConstructor.parameters();
    }

    private static Object getComponentValue(RecordComponentInfo componentInfo, Object value) throws NodeMappingException {
        try {
            return componentInfo.get(value);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new NodeMappingException(e);
        }
    }

    @Override
    public Node toNode(Record recordValue, ToNodeContext context) throws NodeMappingException {
        var objectNode = new ObjectNode();
        for (var recordComponent : recordComponents) {
            var name = recordComponent.name();
            var value = getComponentValue(recordComponent, recordValue);
            //处理忽略 null value
            if (value == null && context.options().ignoreNullValue()) {
                continue;
            }
            objectNode.put(name, context.toNode(name, value));
        }
        return objectNode;
    }

    @Override
    public Record fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理 ObjectNode 类型
        if (node instanceof ObjectNode objectNode) {
            var params = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i = i + 1) {
                var parameter = parameters[i];
                var tempNode = objectNode.get(parameter.name());
                //这里不要把 null 传递到 fromNode 中防止引发错误
                if (tempNode == null) {
                    tempNode = NULL;
                }
                params[i] = context.fromNode(tempNode, parameter.parameterType());
            }
            return newInstance(params);
        }
        //3, 非 ObjectNode 类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

    public ClassInfo classInfo() {
        return classInfo;
    }

    private Record newInstance(Object... params) throws NodeMappingException {
        try {
            return recordConstructor.newInstance(params);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            // 因为我们使用的都是 public 构造函数, 所以这里理论上不会出现异常, 除非构造函数方法内部逻辑异常
            throw new NodeMappingException(e);
        }
    }

}
