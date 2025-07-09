package cool.scx.object.node;

public sealed interface NumericNode extends ValueNode permits IntNode, LongNode, FloatNode, DoubleNode, BigIntegerNode, BigDecimalNode {

    Number numberValue();

}
