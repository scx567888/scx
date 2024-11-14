package cool.scx.common.test;

import cool.scx.common.util.OSHelper;
import org.testng.annotations.Test;

public class OSHelperTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        System.out.println(OSHelper.getOSInfo());
    }

}
