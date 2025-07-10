package cool.scx.object.mapper.primitive;

import cool.scx.object.node.Node;
import cool.scx.object.node.NullNode;
import cool.scx.object.node.TextNode;

public class CharacterNodeMapper extends PrimitiveNodeMapper<Character> {

    public CharacterNodeMapper(boolean isPrimitive) {
        super(isPrimitive, (char) 0);
    }

    @Override
    public Node toNode(Character value) {
        return new TextNode(String.valueOf((char) value));
    }

    @Override
    public Character fromNode0(Node node) {
        if (node == NullNode.NULL) {
            return null;
        }
        if (node instanceof TextNode textNode) {
            var text = textNode.asText();
            if (text.length() == 1) {
                return text.charAt(0);
            }
            throw new IllegalArgumentException("String length must be 1 for char, got: " + text);
        }
        throw new IllegalArgumentException("Invalid node type: " + node.getClass());
    }

}
