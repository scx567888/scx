package cool.scx.object.serializer;

import javax.xml.namespace.QName;

/// XmlNodeSerializerOptions
///
/// @author scx567888
/// @version 0.0.1
public final class XmlNodeSerializerOptions {

    private QName xmlRootTagName;
    // 最大嵌套深度
    private int maxNestingDepth;

    public XmlNodeSerializerOptions() {
        this.xmlRootTagName = QName.valueOf("Root");
        this.maxNestingDepth = 200; // 默认 200 既不会轻易栈溢出, 也足够 99.99% 的情况
    }

    public QName xmlRootTagName() {
        return xmlRootTagName;
    }

    public XmlNodeSerializerOptions xmlRootTagName(QName xmlRootTagName) {
        if (xmlRootTagName == null) {
            throw new NullPointerException("xmlRootTagName cannot be null");
        }
        this.xmlRootTagName = xmlRootTagName;
        return this;
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

}
