package cool.scx.object.node;

public final class LongNode implements NumericNode {

    private final long _value;

    public LongNode(long v) {
        this._value = v;
    }

    public long value() {
        return _value;
    }

    @Override
    public Number numberValue() {
        return _value;
    }
    
}
