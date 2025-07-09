package cool.scx.object.node;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class BigDecimalNode implements NumericNode {

    private final BigDecimal _value;

    public BigDecimalNode(BigDecimal v) {
        this._value = v;
    }

    public BigDecimal value() {
        return _value;
    }

    @Override
    public Number numberValue() {
        return _value;
    }

    @Override
    public int asInt() {
        return _value.intValue();
    }

    @Override
    public long asLong() {
        return _value.longValue();
    }

    @Override
    public float asFloat() {
        return _value.floatValue();
    }

    @Override
    public double asDouble() {
        return _value.doubleValue();
    }

    @Override
    public BigInteger asBigInteger() {
        return _value.toBigInteger();
    }

    @Override
    public BigDecimal asBigDecimal() {
        return _value;
    }

    @Override
    public String asText() {
        return _value.toString();
    }

    @Override
    public boolean asBoolean() {
        return !_value.equals(BigDecimal.ZERO);
    }

    @Override
    public String toString() {
        return asText();
    }

}
