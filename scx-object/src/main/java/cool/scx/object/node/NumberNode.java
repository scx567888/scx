package cool.scx.object.node;

/// NumberNode (语义标记接口)
///
/// @author scx567888
/// @version 0.0.1
public sealed interface NumberNode extends ValueNode permits IntNode, LongNode, FloatNode, DoubleNode, BigIntegerNode, BigDecimalNode {

    @Override
    NumberNode deepCopy();

}
