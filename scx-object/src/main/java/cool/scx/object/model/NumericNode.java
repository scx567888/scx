package cool.scx.object.model;

public sealed interface NumericNode extends ValueNode permits IntNode, LongNode, BigIntegerNode, FloatNode, DoubleNode, BigDecimalNode {

    Number numberValue();

}
