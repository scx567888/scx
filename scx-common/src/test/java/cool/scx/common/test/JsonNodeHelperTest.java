package cool.scx.common.test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.scx.common.jackson.JsonNodeHelper;
import cool.scx.common.util.ObjectUtils;
import org.testng.annotations.Test;

public class JsonNodeHelperTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        ObjectNode objectNode = ObjectUtils.jsonMapper().createObjectNode();
        JsonNodeHelper.set(objectNode, "a.b.c.d.e.f", new int[]{1, 2, 3, 4});
        System.out.println(objectNode);
        System.out.println(JsonNodeHelper.get(objectNode, "a.b.c"));
    }

}
