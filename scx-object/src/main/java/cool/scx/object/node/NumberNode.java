package cool.scx.object.node;

import java.math.BigDecimal;
import java.math.BigInteger;

/// NumberNode (语义标记接口)
///
/// 同时移除 asXXX 上的 throws NumberFormatException (因为不可能发生)
///
/// @author scx567888
/// @version 0.0.1
public sealed interface NumberNode extends ValueNode permits IntNode, LongNode, FloatNode, DoubleNode, BigIntegerNode, BigDecimalNode {

    @Override
    int asInt();

    @Override
    long asLong();

    @Override
    float asFloat();

    @Override
    double asDouble();

    @Override
    BigInteger asBigInteger();

    @Override
    BigDecimal asBigDecimal();

    @Override
    String asText();

    @Override
    boolean asBoolean();

    @Override
    NumberNode deepCopy();

}
