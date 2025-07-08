package cool.scx.object.model;

import java.math.BigInteger;

public class BigIntegerNode implements NumericNode {

    private final BigInteger _value;

    public BigIntegerNode(BigInteger v) {
        this._value = v;
    }
    
}
