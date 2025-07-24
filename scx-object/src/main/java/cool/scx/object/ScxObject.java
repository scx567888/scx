package cool.scx.object;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import cool.scx.object.mapper.*;
import cool.scx.object.node.Node;
import cool.scx.object.parser.NodeParser;
import cool.scx.object.parser.NodeParserOptions;
import cool.scx.object.serializer.NodeSerializer;
import cool.scx.object.serializer.NodeSerializerOptions;
import cool.scx.object.serializer.XmlNodeSerializer;
import cool.scx.object.serializer.XmlNodeSerializerOptions;
import cool.scx.reflect.TypeInfo;
import cool.scx.reflect.TypeReference;

import java.io.File;
import java.io.IOException;

import static cool.scx.object.parser.DuplicateFieldPolicy.COVER;
import static cool.scx.object.parser.DuplicateFieldPolicy.MERGE;
import static cool.scx.reflect.ScxReflect.typeOf;

/// ScxObject
///
/// @author scx567888
/// @version 0.0.1
public final class ScxObject {

    private static final NodeParser JSON_PARSER;
    private static final NodeParser XML_PARSER;
    private static final NodeSerializer JSON_SERIALIZER;
    private static final XmlNodeSerializer XML_SERIALIZER;
    private static final NodeMapperSelector NODE_MAPPER_SELECTOR;

    static {
        var jsonFactory = new JsonFactory();
        var xmlFactory = new XmlFactory();
        JSON_PARSER = new NodeParser(jsonFactory, new NodeParserOptions().duplicateFieldPolicy(COVER));
        XML_PARSER = new NodeParser(xmlFactory, new NodeParserOptions().duplicateFieldPolicy(MERGE));
        JSON_SERIALIZER = new NodeSerializer(jsonFactory, new NodeSerializerOptions());
        XML_SERIALIZER = new XmlNodeSerializer(xmlFactory, new XmlNodeSerializerOptions());
        NODE_MAPPER_SELECTOR = new NodeMapperSelector();
    }

    public static Node fromJson(String json) throws JsonProcessingException {
        return JSON_PARSER.parse(json);
    }

    public static Node fromXml(String xml) throws JsonProcessingException {
        return XML_PARSER.parse(xml);
    }

    public static Node fromJson(File file) throws IOException {
        return JSON_PARSER.parse(file);
    }

    public static Node fromXml(File file) throws IOException {
        return XML_PARSER.parse(file);
    }

    public static String toJson(Node node) throws JsonProcessingException {
        return JSON_SERIALIZER.serializeAsString(node);
    }

    public static String toXml(Node node) throws JsonProcessingException {
        return XML_SERIALIZER.serializeAsString(node);
    }

    public static Node valueToNode(Object value, ToNodeOptionsImpl toNodeOptions) throws NodeMappingException {
        var toNodeContext = new ToNodeContextImpl(NODE_MAPPER_SELECTOR, toNodeOptions);
        return toNodeContext.toNode(value, "$");// 我们用 '$' 表示根节点
    }

    public static <T> T nodeToValue(Node node, TypeInfo type, FromNodeOptionsImpl fromNodeOptions) throws NodeMappingException {
        var fromNodeContext = new FromNodeContextImpl(NODE_MAPPER_SELECTOR, fromNodeOptions);
        return fromNodeContext.fromNode(node, type);
    }

    public static Node valueToNode(Object value) throws NodeMappingException {
        return valueToNode(value, new ToNodeOptionsImpl());
    }

    public static <T> T nodeToValue(Node node, TypeInfo type) throws NodeMappingException {
        return nodeToValue(node, type, new FromNodeOptionsImpl());
    }

    public static <T> T convertValue(Object value, TypeInfo type) throws NodeMappingException {
        var node = valueToNode(value);
        return nodeToValue(node, type);
    }

    public static <T> T fromJson(String json, TypeInfo type) throws JsonProcessingException, NodeMappingException {
        var node = fromJson(json);
        return nodeToValue(node, type);
    }

    public static <T> T fromJson(String json, Class<T> type) throws JsonProcessingException, NodeMappingException {
        return fromJson(json, typeOf(type));
    }

    public static <T> T fromJson(String json, TypeReference<T> type) throws JsonProcessingException, NodeMappingException {
        return fromJson(json, typeOf(type));
    }

    public static String toJson(Object object) throws JsonProcessingException, NodeMappingException {
        var node = valueToNode(object);
        return toJson(node);
    }

    public static String toJson(Object object, ToNodeOptionsImpl toNodeOptions) throws JsonProcessingException, NodeMappingException {
        var node = valueToNode(object, toNodeOptions);
        return toJson(node);
    }

    public static <T> T fromXml(String xml, TypeInfo type) throws JsonProcessingException, NodeMappingException {
        var node = fromXml(xml);
        return nodeToValue(node, type);
    }

    public static <T> T fromXml(String xml, Class<T> type) throws JsonProcessingException, NodeMappingException {
        return fromXml(xml, typeOf(type));
    }

    public static <T> T fromXml(String xml, TypeReference<T> type) throws JsonProcessingException, NodeMappingException {
        return fromXml(xml, typeOf(type));
    }

    public static String toXml(Object object) throws JsonProcessingException, NodeMappingException {
        var node = valueToNode(object);
        return toXml(node);
    }

    public static String toXml(Object object, ToNodeOptionsImpl toNodeOptions) throws JsonProcessingException, NodeMappingException {
        var node = valueToNode(object, toNodeOptions);
        return toXml(node);
    }

    public static <T> T convertValue(Object value, TypeReference<T> type) throws NodeMappingException {
        return convertValue(value, typeOf(type));
    }

    public static <T> T convertValue(Object value, Class<T> type) throws NodeMappingException {
        return convertValue(value, typeOf(type));
    }

}
