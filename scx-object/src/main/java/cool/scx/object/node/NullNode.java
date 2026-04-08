package cool.scx.object.node;

/// NullNode
///
/// @author scx567888
/// @version 0.0.1
public final class NullNode implements Node {

    public final static NullNode NULL = new NullNode();

    /// 私有化构造函数
    private NullNode() {

    }

    /// NullNode 是单例的 返回 this 即可
    @Override
    public NullNode deepCopy() {
        return this;
    }

    @Override
    public String toString() {
        return "null";
    }

}
