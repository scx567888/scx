package cool.scx.object;

import javax.xml.namespace.QName;

public class NodeCodecOptions {

    private QName xmlRootTagName;

    private DuplicateFieldPolicy duplicateFieldPolicy;

    public NodeCodecOptions() {
        this.xmlRootTagName = QName.valueOf("Root");
        this.duplicateFieldPolicy = DuplicateFieldPolicy.COVER;
    }

    public QName xmlRootTagName() {
        return xmlRootTagName;
    }

    public void xmlRootTagName(QName xmlRootTagName) {
        if (xmlRootTagName == null) {
            throw new NullPointerException("xmlRootTagName is null");
        }
        this.xmlRootTagName = xmlRootTagName;
    }

    public DuplicateFieldPolicy duplicateFieldPolicy() {
        return duplicateFieldPolicy;
    }

    public NodeCodecOptions duplicateFieldPolicy(DuplicateFieldPolicy duplicateFieldPolicy) {
        if (duplicateFieldPolicy == null) {
            throw new NullPointerException("duplicateFieldPolicy is null");
        }
        this.duplicateFieldPolicy = duplicateFieldPolicy;
        return this;
    }

    public enum DuplicateFieldPolicy {
        //覆盖
        COVER,
        //忽略
        IGNORE,
        //抛出异常
        THROW,
        //合并
        MERGE
    }

}
