package cool.scx.standard.test;

import cool.scx.standard.HttpHeader;
import cool.scx.util.CaseUtils;
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
