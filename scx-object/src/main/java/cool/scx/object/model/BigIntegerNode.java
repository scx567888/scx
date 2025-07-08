package cool.scx.object.model;

import java.math.BigInteger;

public final class BigIntegerNode implements NumericNode {

    private final BigInteger _value;

    public BigIntegerNode(BigInteger v) {
        this._value = v;
    }

    public BigInteger value() {
        return _value;
    }

    @Override
    public Number numberValue() {
        return _value;
    }

}
