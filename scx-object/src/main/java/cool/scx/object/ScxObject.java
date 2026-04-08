package cool.scx.object;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.core.JsonFactory;
import cool.scx.object.mapping.*;
import cool.scx.object.node.Node;
import cool.scx.object.parser.NodeParseException;
import cool.scx.object.parser.NodeParser;
import cool.scx.object.parser.json.JsonNodeParser;
import cool.scx.object.parser.json.JsonNodeParserOptions;
import cool.scx.object.parser.xml.XmlNodeParser;
import cool.scx.object.parser.xml.XmlNodeParserOptions;
import cool.scx.object.serializer.NodeSerializeException;
import cool.scx.object.serializer.NodeSerializer;
import cool.scx.object.serializer.json.JsonNodeSerializer;
import cool.scx.object.serializer.json.JsonNodeSerializerOptions;
import cool.scx.object.serializer.xml.XmlNodeSerializer;
import cool.scx.object.serializer.xml.XmlNodeSerializerOptions;
import cool.scx.reflect.TypeInfo;
import cool.scx.reflect.TypeReference;

import java.io.File;
import java.io.IOException;

import static cool.scx.object.parser.json.DuplicateFieldPolicy.COVER;
import static cool.scx.reflect.ScxReflect.typeOf;

/// ScxObject
///
/// @author scx567888
/// @version 0.0.1
public final class ScxObject {

    private static final NodeParser JSON_PARSER;
    private static final NodeParser XML_PARSER;
    private static final NodeSerializer JSON_SERIALIZER;
    private static final NodeSerializer XML_SERIALIZER;
    private static final NodeMapperSelector NODE_MAPPER_SELECTOR;

    static {
        var jsonFactory = new JsonFactory();
        JSON_PARSER = new JsonNodeParser(jsonFactory, new JsonNodeParserOptions().duplicateFieldPolicy(COVER));
        XML_PARSER = new XmlNodeParser(new WstxInputFactory(), new XmlNodeParserOptions());
        JSON_SERIALIZER = new JsonNodeSerializer(jsonFactory, new JsonNodeSerializerOptions());
        XML_SERIALIZER = new XmlNodeSerializer(new WstxOutputFactory(), new XmlNodeSerializerOptions());
        NODE_MAPPER_SELECTOR = new NodeMapperSelector();
    }

    public static Node fromJson(String json) throws NodeParseException {
        return JSON_PARSER.parse(json);
    }

    public static Node fromXml(String xml) throws NodeParseException {
        return XML_PARSER.parse(xml);
    }

    public static Node fromJson(File file) throws IOException, NodeParseException {
        return JSON_PARSER.parse(file);
    }

    public static Node fromXml(File file) throws IOException, NodeParseException {
        return XML_PARSER.parse(file);
    }

    public static String toJson(Node node) throws NodeSerializeException {
        return JSON_SERIALIZER.serializeAsString(node);
    }

    public static String toXml(Node node) throws NodeSerializeException {
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

    public static <T> T fromJson(String json, TypeInfo type) throws NodeMappingException, NodeParseException {
        var node = fromJson(json);
        return nodeToValue(node, type);
    }

    public static <T> T fromJson(String json, Class<T> type) throws NodeMappingException, NodeParseException {
        return fromJson(json, typeOf(type));
    }

    public static <T> T fromJson(String json, TypeReference<T> type) throws NodeMappingException, NodeParseException {
        return fromJson(json, typeOf(type));
    }

    public static String toJson(Object object) throws NodeMappingException, NodeSerializeException {
        var node = valueToNode(object);
        return toJson(node);
    }

    public static String toJson(Object object, ToNodeOptionsImpl toNodeOptions) throws NodeMappingException, NodeSerializeException {
        var node = valueToNode(object, toNodeOptions);
        return toJson(node);
    }

    public static <T> T fromXml(String xml, TypeInfo type) throws NodeMappingException, NodeParseException {
        var node = fromXml(xml);
        return nodeToValue(node, type);
    }

    public static <T> T fromXml(String xml, Class<T> type) throws NodeMappingException, NodeParseException {
        return fromXml(xml, typeOf(type));
    }

    public static <T> T fromXml(String xml, TypeReference<T> type) throws NodeMappingException, NodeParseException {
        return fromXml(xml, typeOf(type));
    }

    public static String toXml(Object object) throws NodeMappingException, NodeSerializeException {
        var node = valueToNode(object);
        return toXml(node);
    }

    public static String toXml(Object object, ToNodeOptionsImpl toNodeOptions) throws NodeMappingException, NodeSerializeException {
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
