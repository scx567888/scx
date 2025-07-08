package cool.scx.object.model;

public final class FloatNode implements NumericNode {

    private final float _value;

    public FloatNode(float v) {
        this._value = v;
    }

    public float value() {
        return _value;
    }

    @Override
    public Number numberValue() {
        return _value;
    }

}
