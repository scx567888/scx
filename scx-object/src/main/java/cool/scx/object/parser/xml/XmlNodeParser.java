package cool.scx.object.parser.xml;

import com.ctc.wstx.stax.WstxInputFactory;
import cool.scx.object.node.*;
import cool.scx.object.parser.NodeParseException;
import cool.scx.object.parser.NodeParser;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.StringReader;

import static com.ctc.wstx.api.WstxInputProperties.*;
import static cool.scx.object.parser.xml.AutoCloseableXMLStreamReader.wrap;

/// ### 解析规则:
///
/// 1. `<a></a>` -> `""`
///     没有文本和子元素 -> TextNode("")
///
/// 2. `<a/>` -> `NULL`
///     没有文本和子元素 (自闭合标签) -> NULL
///
/// 3. `<a>123</a>` -> `"123"`
///     只有文本时 -> TextNode
///
/// 4. `<a><b>123</b></a>` -> `{"b": "123"}`
///     存在子元素 -> ObjectNode
///
/// 5. `<a name="jack"></a>` 或 `<a name="jack" />` -> `{"name": "jack"}`
///     存在属性 (无论是否自闭合) -> ObjectNode
///
/// 6. `<a name="jack"><age>18</age></a>` -> `{"name": "jack", "age": "18"}`
///     属性和子元素相同方式看待 -> ObjectNode
///
/// 7. `<a>000<b>123</b></a>` -> `{"b": "123", "": "000"}`
///     同时存在子元素和单个文本 -> 将文本视为单个 TextNode, 并以 "" 为 key 合并到 ObjectNode 中
///
/// 8. `<a>000<b>123</b>6666</a>` -> `{"b": "123", "": ["000", "6666"]}`
///     同时存在子元素和多个文本 -> 将多个文本视为 ArrayNode(TextNode[]) , 并以 "" 为 key 合并到 Object 中
///
/// 9. `<a><b>123</b><b>456</b></a>` -> `{"b": ["123", "456"]}`
///     存在多个同名子元素 -> 合并子元素为 ArrayNode
///
/// 10, `<a name="jack" name="rose"></a>` -> `{"name": ["jack", "rose"]}`
///     存在多个同名属性 -> 合并属性为 ArrayNode
///
/// 11, `<a name="">  <b> 1 2 3 </b>   </a>` -> `{"b": " 1 2 3 ", "name": "" }`
///     所有的纯空白文本节点视为不存在, 但有内容则保留原始文本, 属性永远保留原始文本
public class XmlNodeParser implements NodeParser {

    // 这里我们使用 WstxInputFactory, 因为默认 XMLInputFactory 功能过于羸弱 
    private final WstxInputFactory xmlFactory;
    private final XmlNodeParserOptions options;

    public XmlNodeParser(WstxInputFactory xmlFactory, XmlNodeParserOptions options) {
        this.xmlFactory = xmlFactory;
        this.options = options;
        //有很多的 安全限制 Woodstox  已经覆盖了 我们直接使用
        this.xmlFactory.setProperty(P_MAX_ELEMENT_DEPTH, options.maxNestingDepth());
        this.xmlFactory.setProperty(P_MAX_TEXT_LENGTH, options.maxStringLength());
        this.xmlFactory.setProperty(P_MAX_ATTRIBUTE_SIZE, options.maxStringLength());
        this.xmlFactory.setProperty(P_MAX_CHILDREN_PER_ELEMENT, options.maxChildCount());
        this.xmlFactory.setProperty(P_MAX_ATTRIBUTES_PER_ELEMENT, options.maxChildCount());
        this.xmlFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        this.xmlFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
    }

    @Override
    public Node parse(String xml) throws NodeParseException {
        try (var xmlStreamReader = wrap(xmlFactory.createXMLStreamReader(new StringReader(xml)))) {
            return parse(xmlStreamReader.reader());
        } catch (XMLStreamException e) {
            throw new NodeParseException(e);
        }
    }

    @Override
    public Node parse(File file) throws NodeParseException {
        try (var xmlStreamReader = wrap(xmlFactory.createXMLStreamReader(file))) {
            return parse(xmlStreamReader.reader());
        } catch (XMLStreamException e) {
            throw new NodeParseException(e);
        }
    }

    private Node parse(XMLStreamReader2 reader) throws XMLStreamException {
        // 1, 循环直到找到第一个元素起始 
        while (true) {
            int eventType = reader.getEventType();
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                break;
            }
            reader.next();
        }
        // 2, 解析
        var node = parseElement(reader);

        // 3, 验证是否存在后续多余内容
        while (reader.hasNext()) {
            // 非法内容 Woodstox 会为直接抛异常 无需我们处理
            reader.next();
        }

        return node;
    }

    private Node parseElement(XMLStreamReader2 reader) throws XMLStreamException {
        // 记录出现过的子元素和属性
        var elements = new ObjectNode();

        // 1, 处理当前元素的属性
        int attributeCount = reader.getAttributeCount();
        for (int i = 0; i < attributeCount; i = i + 1) {
            var name = reader.getAttributeLocalName(i);
            var value = reader.getAttributeValue(i);
            // 可能存在重名元素
            var oldChildNode = elements.get(name);
            if (oldChildNode == null) {
                elements.put(name, new TextNode(value));
                continue;
            }
            //我们默认尝试转换成 数组 
            if (oldChildNode instanceof ArrayNode arrayNode) {
                arrayNode.add(new TextNode(value));
            } else {
                var arrayNode = new ArrayNode();
                arrayNode.add(oldChildNode);
                arrayNode.add(new TextNode(value));
                elements.put(name, arrayNode);
            }
        }

        // 2, 判断是否是自闭合标签 
        var emptyElement = reader.isEmptyElement();
        // 自闭合标签 无需处理内部元素 直接返回
        if (emptyElement) {
            // 这里别忘了移动
            reader.next();
            if (elements.isEmpty()) {
                return NullNode.NULL;
            } else {
                return elements;
            }
        }

        // 记录出现过的文本
        var texts = new ArrayNode();

        while (true) {
            var eventType = reader.next();
            // 如果又遇到了一个 ELEMENT 进行递归解析
            if (eventType == XMLStreamConstants.START_ELEMENT) {
                var name = reader.getLocalName();
                var element = parseElement(reader);
                // 可能存在重名元素
                var oldChildNode = elements.get(name);
                if (oldChildNode == null) {
                    elements.put(name, element);
                    continue;
                }
                //我们默认尝试转换成 数组 
                if (oldChildNode instanceof ArrayNode arrayNode) {
                    arrayNode.add(element);
                } else {
                    var arrayNode = new ArrayNode();
                    arrayNode.add(oldChildNode);
                    arrayNode.add(element);
                    elements.put(name, arrayNode);
                }
            } else if (eventType == XMLStreamConstants.CHARACTERS) {
                // 遇到了文本 进行存储
                var text = reader.getText();
                // 忽略空白字符
                if (!text.isBlank()) {
                    texts.add(new TextNode(text));
                }
            } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                // 跳出循环
                break;
            }
            // 其余的 我们全部 当做不存在, 比如注释之类
        }

        // 没有任何子元素
        if (elements.isEmpty()) {
            // 如果文本也是空的
            if (texts.isEmpty()) {
                return new TextNode("");
            }
            // 如果只有一个文本节点
            if (texts.size() == 1) {
                return texts.get(0);
            }
            // 有很多文本节点 (应该不会出现这种情况)
            return texts;
        }
        // 如果只有一个文本节点
        if (texts.size() == 1) {
            elements.put("", texts.get(0));
            return elements;
        } else if (texts.size() > 1) {
            // 如果又很多文本节点 以数组形式添加
            elements.put("", texts);
        }

        return elements;
    }

}
