package cool.scx.object.node;

public final class IntNode implements NumericNode {

    private final int _value;

    public IntNode(int v) {
        this._value = v;
    }

    public int value() {
        return _value;
    }

    @Override
    public Number numberValue() {
        return _value;
    }

}
