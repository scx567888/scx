package cool.scx.object.node;

public final class DoubleNode implements NumericNode {

    private final double _value;

    public DoubleNode(double v) {
        this._value = v;
    }

    public double value() {
        return _value;
    }

    @Override
    public Number numberValue() {
        return _value;
    }

}
