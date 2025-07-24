package cool.scx.object.node;

import java.math.BigDecimal;
import java.math.BigInteger;

/// TextNode
///
/// @author scx567888
/// @version 0.0.1
public final class TextNode implements ValueNode {

    private final String _value;

    public TextNode(String v) {
        if (v == null) {
            throw new NullPointerException("TextNode value cannot be null");
        }
        this._value = v;
    }

    public String value() {
        return _value;
    }

    @Override
    public int asInt() throws NumberFormatException {
        return Integer.parseInt(_value);
    }

    @Override
    public long asLong() throws NumberFormatException {
        return Long.parseLong(_value);
    }

    @Override
    public float asFloat() throws NumberFormatException {
        return Float.parseFloat(_value);
    }

    @Override
    public double asDouble() throws NumberFormatException {
        return Double.parseDouble(_value);
    }

    @Override
    public BigInteger asBigInteger() throws NumberFormatException {
        return new BigInteger(_value);
    }

    @Override
    public BigDecimal asBigDecimal() throws NumberFormatException {
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

    /// 值类型不可变 返回 this 即可
    @Override
    public TextNode deepCopy() {
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof TextNode textNode) {
            return _value.equals(textNode._value);
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
