package cool.scx.http.test;

import cool.scx.http.status.HttpStatus;
import cool.scx.http.status.HttpStatusHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StatusTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        for (var value : HttpStatus.values()) {
            var reasonPhrase = HttpStatusHelper.getReasonPhrase(value);
            if (reasonPhrase == null) {
                Assert.fail("reasonPhrase is null");
            }
        }
    }

}
