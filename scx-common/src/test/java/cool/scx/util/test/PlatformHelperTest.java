package cool.scx.util.test;

import cool.scx.util.PlatformHelper;
import org.testng.annotations.Test;

public class PlatformHelperTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        System.out.println(PlatformHelper.getOSInfo());
    }

}
