package cool.scx.util.test;

import cool.scx.util.RandomUtils;
import org.testng.annotations.Test;

public class RandomUtilsTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        RandomUtils.randomString(1000);
        RandomUtils.randomString(1000, RandomUtils.PoolType.NUMBER);
        RandomUtils.randomString(1000, new String[]{"😂", "🥶", "😡", "🤢"});
        RandomUtils.randomString(1000, "🐷😂🤣123你好😅😍😡");
    }

}
