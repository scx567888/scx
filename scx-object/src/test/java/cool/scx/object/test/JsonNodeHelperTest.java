package cool.scx.object.test;

import cool.scx.object.jackson.JsonNodeHelper;
import cool.scx.object.node.ObjectNode;
import org.testng.annotations.Test;

public class JsonNodeHelperTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var objectNode = new ObjectNode();
        JsonNodeHelper.set(objectNode, "a.b.c.d.e.f", new int[]{1, 2, 3, 4});
        System.out.println(objectNode);
        System.out.println(JsonNodeHelper.get(objectNode, "a.b.c"));
        System.out.println(JsonNodeHelper.get(objectNode, "a.b.c.d.e.f"));
    }

}
