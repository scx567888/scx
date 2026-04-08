package cool.scx.object.serializer.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import cool.scx.object.node.*;
import cool.scx.object.serializer.NodeSerializeException;
import cool.scx.object.serializer.NodeSerializer;

import java.io.IOException;
import java.io.StringWriter;

/// 此序列化器基于递归下降方式进行序列化, 以保证代码的简洁和可维护性.
/// 但 Node 实际上允许自引用, 也就是说存在无限递归导致栈溢出的风险.
/// 因此, 我们通过 NodeSerializerOptions.maxNestingDepth 来间接限制递归深度,
/// 避免超过 JVM 栈限制 (一般超过 3500 层为危险值)
///
/// @author scx567888
/// @version 0.0.1
public final class JsonNodeSerializer implements NodeSerializer {

    private final JsonFactory jsonFactory;
    private final JsonNodeSerializerOptions options;

    public JsonNodeSerializer(JsonFactory jsonFactory, JsonNodeSerializerOptions options) {
        this.jsonFactory = jsonFactory;
        this.options = options;
        //有很多的 安全限制 jackson 已经覆盖了 我们直接使用
        this.jsonFactory.setStreamWriteConstraints(StreamWriteConstraints.builder()
                .maxNestingDepth(options.maxNestingDepth())
                .build());
    }

    @Override
    public String serializeAsString(Node node) throws NodeSerializeException {
        var writer = new StringWriter();
        try {
            serializeAndClose(jsonFactory.createGenerator(writer), node);
        } catch (JsonProcessingException e) {
            throw new NodeSerializeException(e);
        } catch (IOException e) {
            // 在 StringWriter 中, IOException 理论上永远不会发生
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    private void serializeAndClose(JsonGenerator generator, Node node) throws IOException {
        try (generator) {
            writeNode(generator, node);
        }
    }

    private void writeNode(JsonGenerator generator, Node node) throws IOException {
        switch (node) {
            case ObjectNode objectNode -> writeObject(generator, objectNode);
            case ArrayNode arrayNode -> writeArray(generator, arrayNode);
            default -> writeScalar(generator, node);
        }
    }

    private void writeObject(JsonGenerator generator, ObjectNode objectNode) throws IOException {
        generator.writeStartObject();
        for (var e : objectNode) {
            generator.writeFieldName(e.getKey());
            writeNode(generator, e.getValue());
        }
        generator.writeEndObject();
    }

    private void writeArray(JsonGenerator generator, ArrayNode arrayNode) throws IOException {
        generator.writeStartArray();
        for (var item : arrayNode) {
            writeNode(generator, item);
        }
        generator.writeEndArray();
    }

    private void writeScalar(JsonGenerator generator, Node node) throws IOException {
        switch (node) {
            case TextNode textNode -> generator.writeString(textNode.value());
            case IntNode intNode -> generator.writeNumber(intNode.value());
            case LongNode longNode -> generator.writeNumber(longNode.value());
            case FloatNode floatNode -> generator.writeNumber(floatNode.value());
            case DoubleNode doubleNode -> generator.writeNumber(doubleNode.value());
            case BigIntegerNode bigIntegerNode -> generator.writeNumber(bigIntegerNode.value());
            case BigDecimalNode bigDecimalNode -> generator.writeNumber(bigDecimalNode.value());
            case BooleanNode booleanNode -> generator.writeBoolean(booleanNode.value());
            case NullNode _ -> generator.writeNull();
            default -> throw new IOException("Unsupported Node Type: " + node.getClass().getName());
        }
    }

}
