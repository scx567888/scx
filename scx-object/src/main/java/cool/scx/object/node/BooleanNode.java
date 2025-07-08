package cool.scx.object.node;

public class BooleanNode implements ValueNode {

    public final static BooleanNode TRUE = new BooleanNode(true);
    public final static BooleanNode FALSE = new BooleanNode(false);

    private final boolean _value;

    private BooleanNode(boolean v) {
        this._value = v;
    }

    public boolean value() {
        return _value;
    }
    
}
