package cool.scx.object.mapper.time;

import cool.scx.object.mapper.NodeMapper;
import cool.scx.object.mapper.NodeMapperSelector;
import cool.scx.object.node.Node;

import java.time.LocalDateTime;

//todo 待完成
public class LocalDateTimeNodeMapper implements NodeMapper<LocalDateTime> {

    @Override
    public Node toNode(LocalDateTime value, NodeMapperSelector selector) {
        return null;
    }

    @Override
    public LocalDateTime fromNode(Node node, NodeMapperSelector selector) {
        return null;
    }
    
}
