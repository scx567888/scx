package cool.scx.common.standard.test;

import cool.scx.common.standard.HttpFieldName;
import cool.scx.common.util.CaseUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpFieldNameTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        for (var value : HttpFieldName.values()) {
            Assert.assertEquals(value.toString().toLowerCase(), CaseUtils.toKebab(value.name()));
        }
    }

}
