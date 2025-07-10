package cool.scx.object;

import com.fasterxml.jackson.core.*;
import cool.scx.object.node.*;

import java.io.IOException;
import java.io.UncheckedIOException;

import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;

public class NodeParser {

    private final JsonFactory jsonFactory;
    private final NodeParserOptions options;

    public NodeParser(JsonFactory jsonFactory, NodeParserOptions options) {
        this.jsonFactory = jsonFactory;
        this.options = options;
    }

    public Node parse(String json) throws JsonProcessingException {
        try {
            return parseAndClose(jsonFactory.createParser(json));
        } catch (JsonProcessingException e) {
            throw e;
        } catch (IOException e) {
            // 理论上永远不会发生
            throw new UncheckedIOException(e);
        }
    }

    private Node parseAndClose(JsonParser parser) throws IOException {
        try (parser) {
            var firstToken = parser.nextToken();
            if (firstToken == null) {
                throw new JsonParseException(parser, "未检测到任何有效内容");
            }
            var node = parseNode(parser, 0);
            var trailingToken = parser.nextToken();
            if (trailingToken != null) {
                throw new JsonParseException(parser, "检测到多余内容");
            }
            return node;
        }
    }

    private Node parseNode(JsonParser parser, int nestingDepth) throws IOException {
        var currentToken = parser.currentToken();
        var nextNestingDepth = currentToken == START_OBJECT || currentToken == START_ARRAY ? nestingDepth + 1 : nestingDepth;
        if (nextNestingDepth > options.maxNestingDepth()) {
            throw new JsonParseException(parser, "嵌套深度超过限制: 最大 " + options.maxNestingDepth());
        }
        return switch (currentToken) {
            case START_OBJECT -> parseObject(parser, nextNestingDepth);
            case START_ARRAY -> parseArray(parser, nextNestingDepth);
            default -> parseScalar(parser);
        };
    }

    private Node parseObject(JsonParser parser, int nestingDepth) throws IOException {
        var objectNode = new ObjectNode();
        var fieldCount = 0;
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            if (fieldCount >= options.maxFieldCount()) {
                throw new JsonParseException(parser, "对象字段数超过限制: 最大 " + options.maxFieldCount());
            }
            var fieldName = parser.currentName();
            parser.nextToken();
            var childNode = parseNode(parser, nestingDepth);
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

    private Node parseArray(JsonParser parser, int nestingDepth) throws IOException {
        var arrayNode = new ArrayNode();
        var arraySize = 0;
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            if (arraySize >= options.maxArraySize()) {
                throw new JsonParseException(parser, "数组长度超过限制: 最大 " + options.maxArraySize());
            }
            var childNode = parseNode(parser, nestingDepth);
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
