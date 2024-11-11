package cool.scx.common.test;

import cool.scx.common.util.CaseUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CaseUtilsTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var s = "userNameID";

        for (int i = 0; i < 10; i++) {
            s = CaseUtils.toCamel(s);
            Assert.assertEquals(s, "userNameId");

            s = CaseUtils.toKebab(s);
            Assert.assertEquals(s, "user-name-id");

            s = CaseUtils.toSnake(s);
            Assert.assertEquals(s, "user_name_id");

            s = CaseUtils.toPascal(s);
            Assert.assertEquals(s, "UserNameId");
        }

    }

}
