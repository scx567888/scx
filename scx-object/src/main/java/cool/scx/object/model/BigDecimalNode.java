package cool.scx.object.model;

import java.math.BigDecimal;

public final class BigDecimalNode implements NumericNode {

    private final BigDecimal _value;

    public BigDecimalNode(BigDecimal v) {
        this._value = v;
    }

    public BigDecimal value() {
        return _value;
    }

    @Override
    public Number numberValue() {
        return _value;
    }
    
}
