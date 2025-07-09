package cool.scx.object;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import cool.scx.object.NodeCodecOptions.DuplicateFieldPolicy;
import cool.scx.object.node.Node;
import cool.scx.reflect.ScxReflect;
import cool.scx.reflect.TypeInfo;
import cool.scx.reflect.TypeReference;

import static cool.scx.object.NodeCodecOptions.DuplicateFieldPolicy.*;

public final class ScxObject {

    private static final NodeCodec JSON_CODEC = new NodeCodec(new JsonFactory(), new NodeCodecOptions().duplicateFieldPolicy(COVER));

    private static final NodeCodec XML_CODEC = new NodeCodec(new XmlFactory(), new NodeCodecOptions().duplicateFieldPolicy(MERGE));

    private static final NodeMapper NODE_MAPPER = new NodeMapper();

    public static Node fromJson(String json) throws JsonProcessingException {
        return JSON_CODEC.parse(json);
    }

    public static String toJson(Node node) throws JsonProcessingException {
        return JSON_CODEC.serializeAsString(node);
    }

    public static Node fromXml(String xml) throws JsonProcessingException {
        return XML_CODEC.parse(xml);
    }

    public static String toXml(Node node) throws JsonProcessingException {
        return XML_CODEC.serializeAsString(node);
    }

    public static Node valueToNode(Object value) {
        return NODE_MAPPER.valueToNode(value);
    }

    public static <T> T nodeToValue(Node node, TypeInfo type) {
        return NODE_MAPPER.nodeToValue(node, type);
    }

    public static <T> T convertValue(Object value, TypeInfo type) {
        var node = valueToNode(value);
        return nodeToValue(node, type);
    }

    public static <T> T fromJson(String json, TypeInfo type) throws JsonProcessingException {
        var node = fromJson(json);
        return nodeToValue(node, type);
    }

    public static <T> T fromJson(String json, Class<T> type) throws JsonProcessingException {
        return fromJson(json, ScxReflect.getType(type));
    }

    public static <T> T fromJson(String json, TypeReference<T> type) throws JsonProcessingException {
        return fromJson(json, ScxReflect.getType(type));
    }

    public static String toJson(Object object) throws JsonProcessingException {
        var node = valueToNode(object);
        return toJson(node);
    }

    public static <T> T fromXml(String xml, TypeInfo type) throws JsonProcessingException {
        var node = fromXml(xml);
        return nodeToValue(node, type);
    }

    public static <T> T fromXml(String xml, Class<T> type) throws JsonProcessingException {
        return fromXml(xml, ScxReflect.getType(type));
    }

    public static <T> T fromXml(String xml, TypeReference<T> type) throws JsonProcessingException {
        return fromXml(xml, ScxReflect.getType(type));
    }

    public static String toXml(Object object) throws JsonProcessingException {
        var node = valueToNode(object);
        return toXml(node);
    }

    public static <T> T convertValue(Object value, TypeReference<T> type) {
        return convertValue(value, ScxReflect.getType(type));
    }

    public static <T> T convertValue(Object value, Class<T> type) {
        return convertValue(value, ScxReflect.getType(type));
    }

}
