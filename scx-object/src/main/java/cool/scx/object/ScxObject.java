package cool.scx.object;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import cool.scx.object.node.Node;
import cool.scx.reflect.ScxReflect;
import cool.scx.reflect.TypeInfo;
import cool.scx.reflect.TypeReference;

import java.io.IOException;

public class ScxObject {

    private static final NodeParser JSON_PARSER = new NodeParser(new JsonMapper().getFactory());

    private static final NodeParser XML_PARSER = new NodeParser(new XmlMapper().getFactory());

    private static final ObjectConverter OBJECT_CONVERTER = new ObjectConverter();

    public static Node valueToTree(Object value) {
        return OBJECT_CONVERTER.valueToTree(value);
    }

    public static <T> T treeToValue(Node value, TypeInfo type) {
        return OBJECT_CONVERTER.treeToValue(value, type);
    }

    public static Node fromJson(String json) throws IOException {
        return JSON_PARSER.readTree(json);
    }

    public static String toJson(Node node) throws IOException {
        return JSON_PARSER.writeTreeToString(node);
    }

    public static Node fromXML(String xml) throws IOException {
        return XML_PARSER.readTree(xml);
    }

    public static String toXML(Node node) throws IOException {
        return XML_PARSER.writeTreeToString(node);
    }

    public static <T> T convertValue(Object value, TypeReference<T> type) {
        return convertValue(value, ScxReflect.getType(type));
    }

    public static <T> T convertValue(Object value, Class<T> type) {
        return convertValue(value, ScxReflect.getType(type));
    }

    public static <T> T convertValue(Object value, TypeInfo type) {
        var node = valueToTree(value);
        return treeToValue(node, type);
    }

}
