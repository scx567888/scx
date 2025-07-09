package cool.scx.object.node;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class TextNode implements ValueNode {

    private final String _value;

    public TextNode(String v) {
        this._value = v;
    }

    public String value() {
        return _value;
    }

    @Override
    public int asInt() {
        return Integer.parseInt(_value);
    }

    @Override
    public long asLong() {
        return Long.parseLong(_value);
    }

    @Override
    public float asFloat() {
        return Float.parseFloat(_value);
    }

    @Override
    public double asDouble() {
        return Double.parseDouble(_value);
    }

    @Override
    public BigInteger asBigInteger() {
        return new BigInteger(_value);
    }

    @Override
    public BigDecimal asBigDecimal() {
        return new BigDecimal(_value);
    }

    @Override
    public String asText() {
        return _value;
    }

    @Override
    public boolean asBoolean() {
        return Boolean.parseBoolean(_value);
    }

    @Override
    public String toString() {
        return asText();
    }

}
