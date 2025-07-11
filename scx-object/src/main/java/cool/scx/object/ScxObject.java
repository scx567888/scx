package cool.scx.object;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.Node;
import cool.scx.object.parser.NodeParser;
import cool.scx.object.parser.NodeParserOptions;
import cool.scx.object.serializer.NodeSerializer;
import cool.scx.object.serializer.NodeSerializerOptions;
import cool.scx.reflect.ScxReflect;
import cool.scx.reflect.TypeInfo;
import cool.scx.reflect.TypeReference;

import static cool.scx.object.parser.NodeParserOptions.DuplicateFieldPolicy.COVER;
import static cool.scx.object.parser.NodeParserOptions.DuplicateFieldPolicy.MERGE;

public final class ScxObject {

    private static final NodeParser JSON_PARSER;
    private static final NodeParser XML_PARSER;
    private static final NodeSerializer JSON_SERIALIZER;
    private static final NodeSerializer XML_SERIALIZER;

    private static final NodeMapperSelector NODE_MAPPER_SELECTOR;

    static {
        var jsonFactory = new JsonFactory();

        var xmlFactory = new XmlFactory();
        JSON_PARSER = new NodeParser(jsonFactory, new NodeParserOptions().duplicateFieldPolicy(COVER));
        XML_PARSER = new NodeParser(xmlFactory, new NodeParserOptions().duplicateFieldPolicy(MERGE));
        JSON_SERIALIZER = new NodeSerializer(jsonFactory, new NodeSerializerOptions());
        XML_SERIALIZER = new NodeSerializer(xmlFactory, new NodeSerializerOptions());
        NODE_MAPPER_SELECTOR = new NodeMapperSelector();
    }

    public static Node fromJson(String json) throws JsonProcessingException {
        return JSON_PARSER.parse(json);
    }

    public static Node fromXml(String xml) throws JsonProcessingException {
        return XML_PARSER.parse(xml);
    }

    public static String toJson(Node node) throws JsonProcessingException {
        return JSON_SERIALIZER.serializeAsString(node);
    }

    public static String toXml(Node node) throws JsonProcessingException {
        return XML_SERIALIZER.serializeAsString(node);
    }

    public static Node valueToNode(Object value) {
        return NODE_MAPPER_SELECTOR.toNode(value);
    }

    public static <T> T nodeToValue(Node node, TypeInfo type) {
        return NODE_MAPPER_SELECTOR.fromNode(node, type);
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
