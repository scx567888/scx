package cool.scx.test;

import cool.scx.util.CaseUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CaseUtilsTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var s = "userNameID";
        for (int i = 0; i < 999; i++) {
            s = CaseUtils.toCamel(s);
            Assert.assertEquals(s, "userNameId");
            System.out.println(s);

            s = CaseUtils.toKebab(s);
            Assert.assertEquals(s, "user-name-id");
            System.out.println(s);

            s = CaseUtils.toSnake(s);
            Assert.assertEquals(s, "user_name_id");
            System.out.println(s);

            s = CaseUtils.toPascal(s);
            Assert.assertEquals(s, "UserNameId");
            System.out.println(s);
        }

    }

}
