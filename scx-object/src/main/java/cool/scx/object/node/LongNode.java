package cool.scx.object.node;

import java.math.BigDecimal;
import java.math.BigInteger;

/// LongNode
///
/// @author scx567888
/// @version 0.0.1
public final class LongNode implements NumberNode {

    private final long _value;

    public LongNode(long v) {
        this._value = v;
    }

    public long value() {
        return _value;
    }

    @Override
    public int asInt() {
        return (int) _value;
    }

    @Override
    public long asLong() {
        return _value;
    }

    @Override
    public float asFloat() {
        return _value;
    }

    @Override
    public double asDouble() {
        return _value;
    }

    @Override
    public BigInteger asBigInteger() {
        return BigInteger.valueOf(_value);
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

    /// 值类型不可变 返回 this 即可
    @Override
    public LongNode deepCopy() {
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof LongNode longNode) {
            return _value == longNode._value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(_value);
    }

    @Override
    public String toString() {
        return asText();
    }

}
