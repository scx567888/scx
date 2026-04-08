package cool.scx.object.node;

import java.math.BigDecimal;
import java.math.BigInteger;

/// IntNode
///
/// @author scx567888
/// @version 0.0.1
public final class IntNode implements NumberNode {

    private final int _value;

    public IntNode(int v) {
        this._value = v;
    }

    public int value() {
        return _value;
    }

    @Override
    public int asInt() {
        return _value;
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
    public IntNode deepCopy() {
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof IntNode intNode) {
            return _value == intNode._value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(_value);
    }

    @Override
    public String toString() {
        return asText();
    }

}
