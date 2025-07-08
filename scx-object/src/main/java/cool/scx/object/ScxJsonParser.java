package cool.scx.object;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import cool.scx.object.model.*;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.StringWriter;

/// json 解析器 目前 基于 jackson
public class ScxJsonParser {

    private final JsonFactory jsonFactory;

    public ScxJsonParser(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    public Node readTree(String json) throws IOException {
        var parser = jsonFactory.createParser(json);
        try (parser) {
            // 推进到 第一个有意义的 token
            parser.nextToken();
            return parseNode(parser);
        }
    }

    public String writeTreeToString(Node node) throws IOException {
        var stringWriter = new StringWriter();
        var generator = jsonFactory.createGenerator(stringWriter);
        try (generator) {
            // XML 特殊处理
            if (generator instanceof ToXmlGenerator x) {
                x.setNextName(QName.valueOf("Root"));
            }
            writeNode(generator, node);
        }
        return stringWriter.toString();
    }

    private Node parseNode(JsonParser parser) throws IOException {
        return switch (parser.currentToken()) {
            case START_OBJECT -> {
                var objectNode = new ObjectNode();
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    var fieldName = parser.currentName();
                    parser.nextToken();
                    var childNode = parseNode(parser);
                    var oldNode = objectNode.get(fieldName);
                    if (oldNode != null) {
                        handleDuplicateField(fieldName, objectNode, oldNode, childNode);
                    } else {
                        objectNode.put(fieldName, childNode);
                    }
                }
                yield objectNode;
            }
            case START_ARRAY -> {
                var arrayNode = new ArrayNode();
                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    var childNode = parseNode(parser);
                    arrayNode.add(childNode);
                }
                yield arrayNode;
            }
            case VALUE_STRING -> new TextNode(parser.getText());
            case VALUE_NUMBER_INT, VALUE_NUMBER_FLOAT -> switch (parser.getNumberType()) {
                case INT -> new IntNode(parser.getIntValue());
                case LONG -> new LongNode(parser.getLongValue());
                case BIG_INTEGER -> new BigIntegerNode(parser.getBigIntegerValue());
                case FLOAT -> new FloatNode(parser.getFloatValue());
                case DOUBLE -> new DoubleNode(parser.getDoubleValue());
                case BIG_DECIMAL -> new BigDecimalNode(parser.getDecimalValue());
            };
            case VALUE_TRUE -> BooleanNode.TRUE;
            case VALUE_FALSE -> BooleanNode.FALSE;
            case VALUE_NULL -> NullNode.NULL;
            default -> throw new IOException("Unsupported token: " + parser.currentToken());
        };
    }

    private void writeNode(JsonGenerator generator, Node node) throws IOException {
        switch (node) {
            case ObjectNode objectNode -> {
                generator.writeStartObject();
                for (var e : objectNode) {
                    generator.writeFieldName(e.getKey());
                    writeNode(generator, e.getValue());
                }
                generator.writeEndObject();
            }
            case ArrayNode arrayNode -> {
                generator.writeStartArray();
                for (var item : arrayNode) {
                    writeNode(generator, item);
                }
                generator.writeEndArray();
            }
            case TextNode textNode -> generator.writeString(textNode.value());
            case NumericNode numericNode -> {
                switch (numericNode) {
                    case IntNode intNode -> generator.writeNumber(intNode.value());
                    case LongNode longNode -> generator.writeNumber(longNode.value());
                    case BigIntegerNode bigIntegerNode -> generator.writeNumber(bigIntegerNode.value());
                    case FloatNode floatNode -> generator.writeNumber(floatNode.value());
                    case DoubleNode doubleNode -> generator.writeNumber(doubleNode.value());
                    case BigDecimalNode bigDecimalNode -> generator.writeNumber(bigDecimalNode.value());
                }
            }
            case BooleanNode booleanNode -> generator.writeBoolean(booleanNode.value());
            case NullNode _ -> generator.writeNull();
            default -> throw new IOException("Unsupported Node Type: " + node.getClass().getName());
        }
    }

    /// 重复字段 (一般只在 XML 中才会出现)
    private void handleDuplicateField(String fieldName, ObjectNode objectNode, Node oldNode, Node newNode) {
        //我们默认尝试转换成 数组 
        if (oldNode instanceof ArrayNode a) {
            a.add(newNode);
        } else {
            var arrayNode = new ArrayNode();
            arrayNode.add(oldNode);
            arrayNode.add(newNode);
            objectNode.put(fieldName, arrayNode);
        }
    }

}
