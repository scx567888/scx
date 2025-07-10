package cool.scx.object.node;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class BooleanNode implements ValueNode {

    public final static BooleanNode TRUE = new BooleanNode(true);
    public final static BooleanNode FALSE = new BooleanNode(false);

    private final boolean _value;

    private BooleanNode(boolean v) {
        this._value = v;
    }

    public static BooleanNode of(boolean v) {
        return v ? TRUE : FALSE;
    }

    public boolean value() {
        return _value;
    }

    @Override
    public int asInt() {
        return _value ? 1 : 0;
    }

    @Override
    public long asLong() {
        return _value ? 1L : 0L;
    }

    @Override
    public float asFloat() {
        return _value ? 1.0f : 0.0f;
    }

    @Override
    public double asDouble() {
        return _value ? 1.0d : 0.0d;
    }

    @Override
    public BigInteger asBigInteger() {
        return _value ? BigInteger.ONE : BigInteger.ZERO;
    }

    @Override
    public BigDecimal asBigDecimal() {
        return _value ? BigDecimal.ONE : BigDecimal.ZERO;
    }

    @Override
    public String asText() {
        return String.valueOf(_value);
    }

    @Override
    public boolean asBoolean() {
        return _value;
    }

    @Override
    public String toString() {
        return asText();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof BooleanNode booleanNode) {
            return _value == booleanNode._value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(_value);
    }

}
