package cool.scx.object.serializer;

import javax.xml.namespace.QName;

public class NodeSerializerOptions {

    private QName xmlRootTagName;
    // 最大嵌套深度
    private int maxNestingDepth;

    public NodeSerializerOptions() {
        this.xmlRootTagName = QName.valueOf("Root");
        this.maxNestingDepth = 200; // 默认 200 既不会轻易栈溢出, 也足够 99.99% 的情况
    }

    public QName xmlRootTagName() {
        return xmlRootTagName;
    }

    public NodeSerializerOptions xmlRootTagName(QName xmlRootTagName) {
        if (xmlRootTagName == null) {
            throw new NullPointerException("xmlRootTagName is null");
        }
        this.xmlRootTagName = xmlRootTagName;
        return this;
    }

    public int maxNestingDepth() {
        return maxNestingDepth;
    }

    public NodeSerializerOptions maxNestingDepth(int maxNestingDepth) {
        if (maxNestingDepth < 0) {
            throw new IllegalArgumentException("maxNestingDepth cannot < 0");
        }
        this.maxNestingDepth = maxNestingDepth;
        return this;
    }

}
