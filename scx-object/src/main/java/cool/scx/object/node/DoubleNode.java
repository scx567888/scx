package cool.scx.object.node;

import java.math.BigDecimal;
import java.math.BigInteger;

/// DoubleNode
///
/// @author scx567888
/// @version 0.0.1
public final class DoubleNode implements NumberNode {

    private final double _value;

    public DoubleNode(double v) {
        this._value = v;
    }

    public double value() {
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

    /// 值类型不可变 返回 this 即可
    @Override
    public DoubleNode deepCopy() {
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DoubleNode doubleNode) {
            return Double.compare(_value, doubleNode._value) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(_value);
    }

    @Override
    public String toString() {
        return asText();
    }

}
