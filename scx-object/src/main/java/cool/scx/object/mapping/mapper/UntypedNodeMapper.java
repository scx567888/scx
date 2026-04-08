package cool.scx.object.mapping.mapper;

import cool.scx.object.mapping.FromNodeContext;
import cool.scx.object.mapping.NodeMapper;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.mapping.ToNodeContext;
import cool.scx.object.node.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static cool.scx.reflect.ScxReflect.typeOf;

/// 未指定类型的 Object.class
///
/// @author scx567888
/// @version 0.0.1
public final class UntypedNodeMapper implements NodeMapper<Object> {

    @Override
    public Node toNode(Object value, ToNodeContext context) {
        // 实际上 只是一个空的 Object 对象, 我们 转换为一个空的 ObjectNode 
        return new ObjectNode();
    }

    @Override
    public Object fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //我们根据 最适合的类型来处理
        return switch (node) {
            case NullNode _ -> null;
            // ObjectNode 就用 Map
            case ObjectNode _ -> context.fromNode(node, typeOf(Map.class));
            // ArrayNode 就用 List 
            case ArrayNode _ -> context.fromNode(node, typeOf(List.class));
            // 其余字面量选择最接近的
            case IntNode _ -> context.fromNode(node, typeOf(Integer.class));
            case LongNode _ -> context.fromNode(node, typeOf(Long.class));
            case BigIntegerNode _ -> context.fromNode(node, typeOf(BigInteger.class));
            case FloatNode _ -> context.fromNode(node, typeOf(Float.class));
            case DoubleNode _ -> context.fromNode(node, typeOf(Double.class));
            case BigDecimalNode _ -> context.fromNode(node, typeOf(BigDecimal.class));
            case TextNode _ -> context.fromNode(node, typeOf(String.class));
            case BooleanNode _ -> context.fromNode(node, typeOf(Boolean.class));
        };
    }

}
