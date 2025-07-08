package cool.scx.object.model;

public class TextNode implements ValueNode {

    private final String _value;

    public TextNode(String v) {
        this._value = v;
    }

    public String value() {
        return _value;
    }

}
