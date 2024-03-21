package cool.scx.common.standard.test;

import cool.scx.common.standard.HttpHeader;
import cool.scx.common.util.CaseUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpHeaderTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        for (var value : HttpHeader.values()) {
            Assert.assertEquals(value.toString(), CaseUtils.toKebab(value.name()));
        }
    }

}
