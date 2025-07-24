package cool.scx.object.mapper.time;

import cool.scx.object.mapper.FromNodeContext;
import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMappingException;
import cool.scx.object.mapper.ToNodeContext;
import cool.scx.object.node.Node;
import cool.scx.object.node.TextNode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/// LocalDateTimeNodeMapper
///
/// @author scx567888
/// @version 0.0.1
public final class LocalDateTimeNodeMapper implements NodeMapper<LocalDateTime> {

    private final DateTimeFormatter formatter;

    public LocalDateTimeNodeMapper(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public Node toNode(LocalDateTime value, ToNodeContext context) {
        return new TextNode(formatter.format(value));
    }

    @Override
    public LocalDateTime fromNode(Node node, FromNodeContext context) throws NodeMappingException {
        //1, 处理 null
        if (node.isNull()) {
            return null;
        }
        //2, 只处理 TextNode 类型
        if (node instanceof TextNode textNode) {
            try {
                return LocalDateTime.parse(textNode.asText(), formatter);
            } catch (DateTimeParseException e) {
                throw new NodeMappingException(e);
            }
        }
        //3, 非 TextNode 类型无法转换直接报错
        throw new NodeMappingException("Unsupported node type: " + node.getClass());
    }

}
