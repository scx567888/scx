package cool.scx.object.parser.xml;

public class XmlNodeParserOptions {

    // 最大嵌套深度
    private int maxNestingDepth;
    // 最大子元素数量 (同时作用于属性和子元素)
    private int maxChildCount;
    // 最大字符串长度 (同时作用于属性值和文本)
    private int maxStringLength;

    public XmlNodeParserOptions() {
        this.maxNestingDepth = 200; // 默认 200 既不会轻易栈溢出, 也足够 99.99% 的情况
        this.maxChildCount = 5000;
        this.maxStringLength = 2000 * 10000;
    }

    public int maxNestingDepth() {
        return maxNestingDepth;
    }

    public XmlNodeParserOptions maxNestingDepth(int maxNestingDepth) {
        if (maxNestingDepth < 0) {
            throw new IllegalArgumentException("maxNestingDepth cannot < 0");
        }
        this.maxNestingDepth = maxNestingDepth;
        return this;
    }

    public int maxChildCount() {
        return maxChildCount;
    }

    public XmlNodeParserOptions maxChildCount(int maxChildCount) {
        if (maxChildCount < 0) {
            throw new IllegalArgumentException("maxChildCount cannot < 0");
        }
        this.maxChildCount = maxChildCount;
        return this;
    }

    public int maxStringLength() {
        return maxStringLength;
    }

    public XmlNodeParserOptions maxStringLength(int maxStringLength) {
        if (maxStringLength < 0) {
            throw new IllegalArgumentException("maxStringLength cannot < 0");
        }
        this.maxStringLength = maxStringLength;
        return this;
    }

}
