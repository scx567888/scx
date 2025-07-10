package cool.scx.object;

import com.fasterxml.jackson.core.*;
import cool.scx.object.node.*;

import java.io.IOException;
import java.io.UncheckedIOException;

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
        } catch (IOException e) { //理论上永远不可能发生
            throw new UncheckedIOException(e);
        }
    }

    private Node parseAndClose(JsonParser parser) throws IOException {
        try (parser) {
            var firstToken = parser.nextToken();
            if (firstToken == null) {
                throw new JsonParseException("异常输入 !!!");
            }
            var node = parseNode(parser, 0);
            var trailingToken = parser.nextToken();
            if (trailingToken != null) {
                throw new JsonParseException("异常结束 !!!");
            }
            return node;
        }
    }

    private Node parseNode(JsonParser parser, int depth) throws IOException {
        if (depth > options.maxDepth()) {
            throw new JsonParseException("深度超过限制:" + depth);
        }
        var currentToken = parser.currentToken();
        if (currentToken == JsonToken.START_OBJECT) {
            return parseObject(parser, depth + 1);
        } else if (currentToken == JsonToken.START_ARRAY) {
            return parseArray(parser, depth + 1);
        }
        return parseScalar(parser);
    }

    private Node parseObject(JsonParser parser, int depth) throws IOException {
        var objectNode = new ObjectNode();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            var fieldName = parser.currentName();
            parser.nextToken();
            var childNode = parseNode(parser, depth);
            var oldChildNode = objectNode.get(fieldName);
            if (oldChildNode == null) {
                objectNode.put(fieldName, childNode);
            } else {
                handleDuplicateField(fieldName, objectNode, oldChildNode, childNode);
            }
        }
        return objectNode;
    }

    private Node parseArray(JsonParser parser, int depth) throws IOException {
        var arrayNode = new ArrayNode();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            var childNode = parseNode(parser, depth);
            arrayNode.add(childNode);
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
            default -> throw new IOException("Unsupported token: " + currentToken);
        };
    }

    private Node parseInt(JsonParser parser) throws IOException {
        var numberType = parser.getNumberType();
        return switch (numberType) {
            case INT -> new IntNode(parser.getIntValue());
            case LONG -> new LongNode(parser.getLongValue());
            case BIG_INTEGER -> new BigIntegerNode(parser.getBigIntegerValue());
            default -> throw new IOException("Unsupported number type: " + numberType);
        };
    }

    private Node parseFloat(JsonParser parser) throws IOException {
        var numberType = parser.getNumberType();
        return switch (numberType) {
            case FLOAT -> new FloatNode(parser.getFloatValue());
            case DOUBLE -> new DoubleNode(parser.getDoubleValue());
            case BIG_DECIMAL -> new BigDecimalNode(parser.getDecimalValue());
            default -> throw new IOException("Unsupported number type: " + numberType);
        };
    }

    /// 重复字段 (一般只在 XML 中才会出现)
    private void handleDuplicateField(String fieldName, ObjectNode parentNode, Node oldChildNode, Node newChildNode) {
        var duplicateFieldPolicy = options.duplicateFieldPolicy();
        switch (duplicateFieldPolicy) {
            case COVER -> {
                parentNode.put(fieldName, newChildNode);
            }
            case IGNORE -> {
                // 什么都不做 
            }
            case THROW -> {
                throw new IllegalStateException("检测到重复 Field");
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
