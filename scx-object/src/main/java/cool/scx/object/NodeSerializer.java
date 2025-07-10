package cool.scx.object;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import cool.scx.object.node.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;

public class NodeSerializer {

    private final JsonFactory jsonFactory;
    private final NodeSerializerOptions options;

    public NodeSerializer(JsonFactory jsonFactory, NodeSerializerOptions options) {
        this.jsonFactory = jsonFactory;
        this.options = options;
        //有很多的 安全限制 jackson 已经覆盖了 我们直接使用
        this.jsonFactory.setStreamWriteConstraints(StreamWriteConstraints.builder()
                .maxNestingDepth(1)
                .build());
    }

    public String serializeAsString(Node node) throws JsonProcessingException {
        var writer = new StringWriter();
        try {
            serializeAndClose(jsonFactory.createGenerator(writer), node);
        } catch (JsonProcessingException e) {
            throw e;
        } catch (IOException e) {
            // 理论上永远不会发生
            throw new UncheckedIOException(e);
        }
        return writer.toString();
    }

    private void serializeAndClose(JsonGenerator generator, Node node) throws IOException {
        try (generator) {
            // XML 特殊处理
            if (generator instanceof ToXmlGenerator x) {
                x.setNextName(options.xmlRootTagName());
            }
            writeNode(generator, node);
        }
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

}
