package cool.scx.object.parser.json;

import com.fasterxml.jackson.core.*;
import cool.scx.object.node.*;
import cool.scx.object.parser.NodeParseException;
import cool.scx.object.parser.NodeParser;

import java.io.File;
import java.io.IOException;

/// 此解析器基于递归下降方式进行解析, 以保证代码的简洁和可维护性.
/// 但递归解析存在栈溢出的风险, 特别是在嵌套层数较大时.
/// 因此, 我们通过 NodeParserOptions.maxNestingDepth 来间接限制递归深度,
/// 避免超过 JVM 栈限制 (一般超过 3500 层为危险值)
///
/// @author scx567888
/// @version 0.0.1
public final class JsonNodeParser implements NodeParser {

    private final JsonFactory jsonFactory;
    private final JsonNodeParserOptions options;

    public JsonNodeParser(JsonFactory jsonFactory, JsonNodeParserOptions options) {
        this.jsonFactory = jsonFactory;
        this.options = options;
        //有很多的 安全限制 jackson 已经覆盖了 我们直接使用
        this.jsonFactory.setStreamReadConstraints(StreamReadConstraints.builder()
                .maxNestingDepth(options.maxNestingDepth())
                .maxStringLength(options.maxStringLength())
                .maxNumberLength(options.maxNumberLength())
                .maxNameLength(options.maxFieldNameLength())
                .build());
    }

    @Override
    public Node parse(String json) throws NodeParseException {
        try {
            return parseAndClose(jsonFactory.createParser(json));
        } catch (JsonProcessingException e) {
            throw new NodeParseException(e);
        } catch (IOException e) {
            // 理论上永远不会发生
            throw new RuntimeException(e);
        }
    }

    @Override
    public Node parse(File file) throws NodeParseException, IOException {
        try {
            return parseAndClose(jsonFactory.createParser(file));
        } catch (JsonProcessingException e) {
            throw new NodeParseException(e);
        }
    }

    private Node parseAndClose(JsonParser parser) throws IOException {
        try (parser) {
            var firstToken = parser.nextToken();
            if (firstToken == null) {
                throw new JsonParseException(parser, "未检测到任何有效内容");
            }
            var node = parseNode(parser);
            var trailingToken = parser.nextToken();
            if (trailingToken != null) {
                throw new JsonParseException(parser, "检测到多余内容");
            }
            return node;
        }
    }

    private Node parseNode(JsonParser parser) throws IOException {
        var currentToken = parser.currentToken();
        return switch (currentToken) {
            case START_OBJECT -> parseObject(parser);
            case START_ARRAY -> parseArray(parser);
            default -> parseScalar(parser);
        };
    }

    private Node parseObject(JsonParser parser) throws IOException {
        var objectNode = new ObjectNode();
        var fieldCount = 0;
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            if (fieldCount >= options.maxFieldCount()) {
                throw new JsonParseException(parser, "对象字段数超过限制: 最大 " + options.maxFieldCount());
            }
            var fieldName = parser.currentName();
            parser.nextToken();
            var childNode = parseNode(parser);
            var oldChildNode = objectNode.get(fieldName);
            if (oldChildNode == null) {
                objectNode.put(fieldName, childNode);
            } else {
                handleDuplicateField(parser, fieldName, objectNode, oldChildNode, childNode);
            }
            fieldCount = fieldCount + 1;
        }
        return objectNode;
    }

    private Node parseArray(JsonParser parser) throws IOException {
        var arrayNode = new ArrayNode();
        var arraySize = 0;
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            if (arraySize >= options.maxArraySize()) {
                throw new JsonParseException(parser, "数组长度超过限制: 最大 " + options.maxArraySize());
            }
            var childNode = parseNode(parser);
            arrayNode.add(childNode);
            arraySize = arraySize + 1;
        }
        return arrayNode;
    }

    private Node parseScalar(JsonParser parser) throws IOException {
        var currentToken = parser.currentToken();
        return switch (currentToken) {
            case VALUE_STRING -> new TextNode(parser.getText());
            case VALUE_NUMBER_INT -> parseInt(parser);
            case VALUE_NUMBER_FLOAT -> parseFloat(parser);
            case VALUE_TRUE -> BooleanNode.TRUE;
            case VALUE_FALSE -> BooleanNode.FALSE;
            case VALUE_NULL -> NullNode.NULL;
            // 理论上永远不会发生
            default -> throw new JsonParseException(parser, "Unsupported token: " + currentToken);
        };
    }

    private Node parseInt(JsonParser parser) throws IOException {
        var numberType = parser.getNumberType();
        return switch (numberType) {
            case INT -> new IntNode(parser.getIntValue());
            case LONG -> new LongNode(parser.getLongValue());
            case BIG_INTEGER -> new BigIntegerNode(parser.getBigIntegerValue());
            // 理论上永远不会发生
            default -> throw new JsonParseException(parser, "Unsupported number type: " + numberType);
        };
    }

    private Node parseFloat(JsonParser parser) throws IOException {
        var numberType = parser.getNumberType();
        return switch (numberType) {
            case FLOAT -> new FloatNode(parser.getFloatValue());
            case DOUBLE -> new DoubleNode(parser.getDoubleValue());
            case BIG_DECIMAL -> new BigDecimalNode(parser.getDecimalValue());
            // 理论上永远不会发生
            default -> throw new JsonParseException(parser, "Unsupported number type: " + numberType);
        };
    }

    /// 处理重复字段 (一般只在 XML 中才会出现)
    private void handleDuplicateField(JsonParser parser, String fieldName, ObjectNode parentNode, Node oldChildNode, Node newChildNode) throws JsonParseException {
        var duplicateFieldPolicy = options.duplicateFieldPolicy();
        switch (duplicateFieldPolicy) {
            case COVER -> {
                parentNode.put(fieldName, newChildNode);
            }
            case IGNORE -> {
                // 什么都不做 
            }
            case THROW -> {
                throw new JsonParseException(parser, "检测到重复字段: \"" + fieldName + "\"");
            }
            case MERGE -> {
                //我们默认尝试转换成 数组 
                if (oldChildNode instanceof ArrayNode arrayNode) {
                    arrayNode.add(newChildNode);
                } else {
                    var arrayNode = new ArrayNode();
                    arrayNode.add(oldChildNode);
                    arrayNode.add(newChildNode);
                    parentNode.put(fieldName, arrayNode);
                }
            }
        }
    }

}
