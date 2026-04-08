package cool.scx.object.serializer.xml;

/// XmlNodeSerializerOptions
///
/// @author scx567888
/// @version 0.0.1
public final class XmlNodeSerializerOptions {

    // 最大嵌套深度
    private int maxNestingDepth;
    // 根节点名称
    private String rootName;
    // 匿名元素 名称
    private String itemName;

    public XmlNodeSerializerOptions() {
        this.maxNestingDepth = 200; // 默认 200 既不会轻易栈溢出, 也足够 99.99% 的情况
        this.rootName = "root";
        this.itemName = "item";
    }

    public int maxNestingDepth() {
        return maxNestingDepth;
    }

    public XmlNodeSerializerOptions maxNestingDepth(int maxNestingDepth) {
        if (maxNestingDepth < 0) {
            throw new IllegalArgumentException("maxNestingDepth cannot < 0");
        }
        this.maxNestingDepth = maxNestingDepth;
        return this;
    }

    public String rootName() {
        return rootName;
    }

    public XmlNodeSerializerOptions rootName(String rootName) {
        if (rootName == null) {
            throw new NullPointerException("rootName cannot be null");
        }
        this.rootName = rootName;
        return this;
    }

    public String itemName() {
        return itemName;
    }

    public XmlNodeSerializerOptions itemName(String itemName) {
        if (itemName == null) {
            throw new NullPointerException("itemName cannot be null");
        }
        this.itemName = itemName;
        return this;
    }

}
