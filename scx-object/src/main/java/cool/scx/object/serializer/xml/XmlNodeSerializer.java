package cool.scx.object.serializer.xml;

import com.ctc.wstx.stax.WstxOutputFactory;
import cool.scx.object.node.*;
import cool.scx.object.serializer.NodeSerializeException;
import cool.scx.object.serializer.NodeSerializer;
import org.codehaus.stax2.XMLStreamWriter2;

import javax.xml.stream.XMLStreamException;
import java.io.StringWriter;

import static cool.scx.object.serializer.xml.AutoCloseableXMLStreamWriter.wrap;

/// ### 序列化规则
///
/// 0. 跟标签默认 -> root, 没有上下文 key 可用的数组 默认 -> item.
///    支持外部配置, 防止某些情况下的冲突
///
/// 1. "123" -> <root>123</root>
///    值类型 -> 标准标签
///
/// 2. NULL -> <root/>
///    NULL -> 闭合标签
///
/// 3. {"a": 123} -> <root><a>123</a></root>
///    对象类型 -> 嵌套标签
///
/// 4. {"a": [1, 2]} -> <root><a>1</a><a>2</a></root>
///    数组 -> 尝试重复
///
/// 5. [1, 2] -> <root><item>1</item><item>2</item></root>
///    数组没有可用的上文 key -> 使用 item
///
/// 6. [1, [2]] -> <root><item>1</item><item><item>2</item></item></root>
///    嵌套数组不进行任何扁平化
///
/// 7, {"": 123} -> <root>123</root>
///    key 为 "", 直接解包
public final class XmlNodeSerializer implements NodeSerializer {

    private final WstxOutputFactory xmlFactory;
    private final XmlNodeSerializerOptions options;

    public XmlNodeSerializer(WstxOutputFactory xmlFactory, XmlNodeSerializerOptions options) {
        this.xmlFactory = xmlFactory;
        this.options = options;
    }

    @Override
    public String serializeAsString(Node node) throws NodeSerializeException {
        var writer = new StringWriter();
        try (var xmlStreamWriter = wrap(xmlFactory.createXMLStreamWriter(writer))) {
            serialize(xmlStreamWriter.writer(), node);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    private void serialize(XMLStreamWriter2 writer2, Node node) throws XMLStreamException {
        // 顶级数组需要特殊处理
        var isRootArray = node instanceof ArrayNode;
        writeNode(writer2, node, options.rootName(), isRootArray, 1);
    }

    private void writeNode(XMLStreamWriter2 writer2, Node node, String key, boolean inArray, int currentDepth) throws XMLStreamException {
        if (currentDepth > options.maxNestingDepth()) {
            throw new NodeSerializeException("Nesting depth exceeds limit: " + options.maxNestingDepth());
        }
        switch (node) {
            case NullNode _ -> {
                // 如果根节点本身就是 null, 直接返回自闭合标签
                writer2.writeEmptyElement(key);
            }
            case ValueNode valueNode -> {
                // "", 直接解包
                if (key.isEmpty()) {
                    writer2.writeCharacters(valueNode.toString());
                } else {
                    writer2.writeStartElement(key);
                    writer2.writeCharacters(valueNode.toString());
                    writer2.writeEndElement();
                }
            }
            case ObjectNode objectNode -> {
                writer2.writeStartElement(key);
                for (var e : objectNode) {
                    writeNode(writer2, e.getValue(), e.getKey(), false, currentDepth + 1);
                }
                writer2.writeEndElement();
            }
            case ArrayNode arrayNode -> {
                if (inArray) {
                    writer2.writeStartElement(key);
                    for (var e : arrayNode) {
                        writeNode(writer2, e, options.itemName(), true, currentDepth + 1);
                    }
                    writer2.writeEndElement();
                } else {
                    for (var e : arrayNode) {
                        writeNode(writer2, e, key, true, currentDepth + 1);
                    }
                }
            }
        }
    }

}
