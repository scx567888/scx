package cool.scx.object.node;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class DoubleNode implements NumericNode {

    private final double _value;

    public DoubleNode(double v) {
        this._value = v;
    }

    public double value() {
        return _value;
    }

    @Override
    public Number numberValue() {
        return _value;
    }

    @Override
    public int asInt() {
        return (int) _value;
    }

    @Override
    public long asLong() {
        return (long) _value;
    }

    @Override
    public float asFloat() {
        return (float) _value;
    }

    @Override
    public double asDouble() {
        return _value;
    }

    @Override
    public BigInteger asBigInteger() {
        return BigDecimal.valueOf(_value).toBigInteger();
    }

    @Override
    public BigDecimal asBigDecimal() {
        return BigDecimal.valueOf(_value);
    }

    @Override
    public String asText() {
        return String.valueOf(_value);
    }

    @Override
    public boolean asBoolean() {
        return _value != 0;
    }

    @Override
    public String toString() {
        return asText();
    }

}
