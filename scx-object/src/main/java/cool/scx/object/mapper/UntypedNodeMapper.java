package cool.scx.object.mapper;

import cool.scx.object.node.*;
import cool.scx.reflect.ScxReflect;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/// 未指定类型的 Object.class
public class UntypedNodeMapper implements NodeMapper<Object> {

    @Override
    public Node toNode(Object value, NodeMapperSelector selector) {
        // 实际上 只是一个空的 Object 对象, 我们 转换为一个空的 ObjectNode 
        return new ObjectNode();
    }

    @Override
    public Object fromNode(Node node, NodeMapperSelector selector) {
        //我们根据 最适合的类型来处理
        return switch (node) {
            case NullNode _ -> null;
            // ObjectNode 就用 Map
            case ObjectNode _ -> selector.fromNode(node, ScxReflect.getType(Map.class));
            // ArrayNode 就用 List 
            case ArrayNode _ -> selector.fromNode(node, ScxReflect.getType(List.class));
            // 其余字面量选择最接近的
            case IntNode _ -> selector.fromNode(node, ScxReflect.getType(Integer.class));
            case LongNode _ -> selector.fromNode(node, ScxReflect.getType(Long.class));
            case BigIntegerNode _ -> selector.fromNode(node, ScxReflect.getType(BigInteger.class));
            case FloatNode _ -> selector.fromNode(node, ScxReflect.getType(Float.class));
            case DoubleNode _ -> selector.fromNode(node, ScxReflect.getType(Double.class));
            case BigDecimalNode _ -> selector.fromNode(node, ScxReflect.getType(BigDecimal.class));
            case TextNode _ -> selector.fromNode(node, ScxReflect.getType(String.class));
            case BooleanNode _ -> selector.fromNode(node, ScxReflect.getType(Boolean.class));
        };
    }

}
