package cool.scx.object.test;

import cool.scx.object.NodeHelper;
import cool.scx.object.node.ObjectNode;
import org.testng.annotations.Test;

public class NodeHelperTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var objectNode = new ObjectNode();
        NodeHelper.set(objectNode, "a.b.c.d.e.f", new int[]{1, 2, 3, 4});
        System.out.println(objectNode);
        System.out.println(NodeHelper.get(objectNode, "a.b.c"));
        System.out.println(NodeHelper.get(objectNode, "a.b.c.d.e.f"));
    }

}
