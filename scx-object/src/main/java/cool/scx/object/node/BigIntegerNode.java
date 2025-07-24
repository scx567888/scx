package cool.scx.object.node;

import java.math.BigDecimal;
import java.math.BigInteger;

/// BigIntegerNode
///
/// @author scx567888
/// @version 0.0.1
public final class BigIntegerNode implements NumberNode {

    private final BigInteger _value;

    public BigIntegerNode(BigInteger v) {
        if (v == null) {
            throw new NullPointerException("BigIntegerNode value cannot be null");
        }
        this._value = v;
    }

    public BigInteger value() {
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
        return _value;
    }

    @Override
    public BigDecimal asBigDecimal() {
        return new BigDecimal(_value);
    }

    @Override
    public String asText() {
        return _value.toString();
    }

    @Override
    public boolean asBoolean() {
        return !_value.equals(BigInteger.ZERO);
    }

    /// 值类型不可变 返回 this 即可
    @Override
    public BigIntegerNode deepCopy() {
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof BigIntegerNode bigIntegerNode) {
            return _value.equals(bigIntegerNode._value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return _value.hashCode();
    }

    @Override
    public String toString() {
        return asText();
    }

}
