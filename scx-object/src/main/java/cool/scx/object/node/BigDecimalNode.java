package cool.scx.object.node;

import java.math.BigDecimal;
import java.math.BigInteger;

/// BigDecimalNode
///
/// @author scx567888
/// @version 0.0.1
public final class BigDecimalNode implements NumberNode {

    private final BigDecimal _value;

    public BigDecimalNode(BigDecimal v) {
        if (v == null) {
            throw new NullPointerException("BigDecimalNode value cannot be null");
        }
        this._value = v;
    }

    public BigDecimal value() {
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

    /// 值类型不可变 返回 this 即可
    @Override
    public BigDecimalNode deepCopy() {
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof BigDecimalNode bigDecimalNode) {
            return _value.equals(bigDecimalNode._value);
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
