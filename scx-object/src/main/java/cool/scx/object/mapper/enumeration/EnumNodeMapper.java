package cool.scx.object.mapper.enumeration;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.Node;
import cool.scx.object.node.TextNode;
import cool.scx.reflect.ClassInfo;

/// EnumNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class EnumNodeMapper<E extends Enum<E>> implements NodeMapper<E> {

    private final ClassInfo classInfo;
    private final Class<E> enumClass;

    @SuppressWarnings("unchecked")
    public EnumNodeMapper(ClassInfo classInfo) {
        this.classInfo = classInfo;
        this.enumClass = (Class<E>) classInfo.enumClass().rawClass();
    }

    @Override
    public Node toNode(E value, ToNodeContext context) {
        return new TextNode(value.name());
    }

    @Override
    public E fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null 
        if (node.isNull()) {
            return null;
        }
        //2, 只处理 TextNode 类型
        if (node instanceof TextNode textNode) {
            var value = textNode.value();
            try {
                return Enum.valueOf(enumClass, value);
            } catch (IllegalArgumentException e) {
                throw new NodeMappingException(e);
            }
        }
        //3, 非 TextNode 类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

    public ClassInfo classInfo() {
        return classInfo;
    }

}
