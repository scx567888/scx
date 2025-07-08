package cool.scx.object.model;

import java.math.BigDecimal;

public class BigDecimalNode implements NumericNode {

    private final BigDecimal _value;

    public BigDecimalNode(BigDecimal v) {
        this._value = v;
    }
    
}
