package cool.scx.object.node;

public sealed interface NumericNode extends ValueNode permits IntNode, LongNode, BigIntegerNode, FloatNode, DoubleNode, BigDecimalNode {

    Number numberValue();

}
