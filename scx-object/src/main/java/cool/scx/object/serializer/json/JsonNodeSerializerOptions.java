package cool.scx.object.serializer.json;

/// JsonNodeSerializerOptions
///
/// @author scx567888
/// @version 0.0.1
public final class JsonNodeSerializerOptions {

    // 最大嵌套深度
    private int maxNestingDepth;

    public JsonNodeSerializerOptions() {
        this.maxNestingDepth = 200; // 默认 200 既不会轻易栈溢出, 也足够 99.99% 的情况
    }

    public int maxNestingDepth() {
        return maxNestingDepth;
    }

    public JsonNodeSerializerOptions maxNestingDepth(int maxNestingDepth) {
        if (maxNestingDepth < 0) {
            throw new IllegalArgumentException("maxNestingDepth cannot < 0");
        }
        this.maxNestingDepth = maxNestingDepth;
        return this;
    }

}
