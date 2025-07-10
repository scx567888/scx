package cool.scx.object.node;

public final class NullNode implements Node {

    public final static NullNode NULL = new NullNode();

    @Override
    public String toString() {
        return "null";
    }
    
}
