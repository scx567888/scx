package cool.scx.object.node;

import java.math.BigDecimal;
import java.math.BigInteger;

public sealed interface ValueNode extends Node permits BooleanNode, NumericNode, TextNode {

    int asInt();

    long asLong();

    float asFloat();

    double asDouble();

    BigInteger asBigInteger();

    BigDecimal asBigDecimal();

    String asText();

    boolean asBoolean();

}
